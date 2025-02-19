import os
from pymongo import MongoClient
from pymongo.errors import ConnectionFailure, OperationFailure
from typing import List, Dict, Optional, Any
import logging

class CRUDOperations:
    def __init__(self, uri: str, db_name: str, collection_name: str):
        """
        Initializes the connection to the MongoDB database with enhanced error handling
        and logging capabilities.
        
        Args:
            uri (str): MongoDB connection URI
            db_name (str): Name of the database
            collection_name (str): Name of the collection
        """
        self.logger = self._setup_logger()
        try:
            self.client = MongoClient(uri)
            # Test the connection
            self.client.admin.command('ping')
            self.db = self.client[db_name]
            self.collection = self.db[collection_name]
            self.logger.info(f"Successfully connected to {db_name}.{collection_name}")
        except ConnectionFailure as e:
            self.logger.error(f"Failed to connect to MongoDB: {e}")
            raise Exception(f"Failed to connect to MongoDB: {e}")

    def _setup_logger(self) -> logging.Logger:
        """Sets up logging configuration."""
        logger = logging.getLogger('CRUDOperations')
        logger.setLevel(logging.INFO)
        if not logger.handlers:
            handler = logging.StreamHandler()
            formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
            handler.setFormatter(formatter)
            logger.addHandler(handler)
        return logger

    def create(self, data: Dict[str, Any]) -> Optional[str]:
        """
        Creates a new document in the collection.
        
        Args:
            data (dict): Document to insert
            
        Returns:
            str: ID of inserted document if successful, None otherwise
        """
        try:
            result = self.collection.insert_one(data)
            self.logger.info(f"Successfully inserted document with ID: {result.inserted_id}")
            return str(result.inserted_id)
        except OperationFailure as e:
            self.logger.error(f"Failed to insert document: {e}")
            return None

    def read(self, query: Dict[str, Any], projection: Dict[str, Any] = None) -> List[Dict[str, Any]]:
        """
        Reads documents from the collection with enhanced querying capabilities.
        
        Args:
            query (dict): Query criteria
            projection (dict): Fields to include/exclude
            
        Returns:
            list: List of matching documents
        """
        try:
            cursor = self.collection.find(query, projection)
            results = list(cursor)
            self.logger.info(f"Successfully retrieved {len(results)} documents")
            return results
        except OperationFailure as e:
            self.logger.error(f"Failed to read documents: {e}")
            return []

    def update(self, query: Dict[str, Any], update_data: Dict[str, Any], upsert: bool = False) -> Dict[str, int]:
        """
        Updates documents in the collection with enhanced options.
        
        Args:
            query (dict): Query criteria
            update_data (dict): Data to update
            upsert (bool): Whether to insert if document doesn't exist
            
        Returns:
            dict: Update operation statistics
        """
        try:
            result = self.collection.update_many(
                query, 
                {"$set": update_data},
                upsert=upsert
            )
            stats = {
                "matched_count": result.matched_count,
                "modified_count": result.modified_count,
                "upserted_id": str(result.upserted_id) if result.upserted_id else None
            }
            self.logger.info(f"Update operation stats: {stats}")
            return stats
        except OperationFailure as e:
            self.logger.error(f"Failed to update documents: {e}")
            return {"matched_count": 0, "modified_count": 0, "upserted_id": None}

    def delete(self, query: Dict[str, Any]) -> Dict[str, int]:
        """
        Deletes documents from the collection with enhanced reporting.
        
        Args:
            query (dict): Query criteria
            
        Returns:
            dict: Deletion operation statistics
        """
        try:
            result = self.collection.delete_many(query)
            stats = {"deleted_count": result.deleted_count}
            self.logger.info(f"Delete operation stats: {stats}")
            return stats
        except OperationFailure as e:
            self.logger.error(f"Failed to delete documents: {e}")
            return {"deleted_count": 0}

    def aggregate(self, pipeline: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """
        Performs aggregation operations on the collection.
        
        Args:
            pipeline (list): Aggregation pipeline stages
            
        Returns:
            list: Aggregation results
        """
        try:
            results = list(self.collection.aggregate(pipeline))
            self.logger.info(f"Successfully performed aggregation, got {len(results)} results")
            return results
        except OperationFailure as e:
            self.logger.error(f"Failed to perform aggregation: {e}")
            return []

    def __del__(self):
        """Cleanup method to properly close the MongoDB connection."""
        if hasattr(self, 'client'):
            self.client.close()
            self.logger.info("MongoDB connection closed")