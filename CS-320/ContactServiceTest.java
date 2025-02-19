import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class EnhancedContactServiceTest {
    private ContactService service;
    private Contact contact1, contact2, contact3;

    @BeforeEach
    void setUp() {
        service = new ContactService();
        contact1 = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        contact2 = new Contact("2", "Jane", "Doe", "2345678901", "456 Oak St");
        contact3 = new Contact("3", "Bob", "Smith", "3456789012", "789 Pine St");
    }

    @Test
    void testSearchByName() {
        service.addContact(contact1);
        service.addContact(contact2);
        service.addContact(contact3);

        List<Contact> results = service.searchContacts("doe", SearchType.NAME);
        assertEquals(2, results.size());
        assertTrue(results.contains(contact1));
        assertTrue(results.contains(contact2));
    }

    @Test
    void testSearchByPhone() {
        service.addContact(contact1);
        service.addContact(contact2);

        List<Contact> results = service.searchContacts("234", SearchType.PHONE);
        assertEquals(1, results.size());
        assertEquals(contact2, results.get(0));
    }

    @Test
    void testSearchByAddress() {
        service.addContact(contact1);
        service.addContact(contact2);
        service.addContact(contact3);

        List<Contact> results = service.searchContacts("st", SearchType.ADDRESS);
        assertEquals(3, results.size());
    }

    @Test
    void testQuickSort() {
        service.addContact(contact1);
        service.addContact(contact2);
        service.addContact(contact3);

        List<Contact> sorted = service.getSortedContacts(SortType.QUICKSORT);
        assertEquals(3, sorted.size());
        assertEquals(contact1, sorted.get(0)); // "Doe" comes before "Smith"
        assertEquals(contact2, sorted.get(1));
        assertEquals(contact3, sorted.get(2));
    }

    @Test
    void testMergeSort() {
        service.addContact(contact3);
        service.addContact(contact1);
        service.addContact(contact2);

        List<Contact> sorted = service.getSortedContacts(SortType.MERGESORT);
        assertEquals(3, sorted.size());
        assertEquals(contact1, sorted.get(0));
        assertEquals(contact2, sorted.get(1));
        assertEquals(contact3, sorted.get(2));
    }

    @Test
    void testContactGroups() {
        service.addContact(contact1);
        service.addContact(contact2);
        service.addToGroup("family", "1");
        service.addToGroup("family", "2");

        Set<Contact> familyGroup = service.getGroupContacts("family");
        assertEquals(2, familyGroup.size());
        assertTrue(familyGroup.contains(contact1));
        assertTrue(familyGroup.contains(contact2));
    }

    @Test
    void testBinarySearchTree() {
        service.addContact(contact3);
        service.addContact(contact1);
        service.addContact(contact2);

        List<Contact> results = service.searchContacts("doe", SearchType.NAME);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(c -> c.getLastName().equals("Doe")));
    }

    @Test
    void testDeleteContactFromGroup() {
        service.addContact(contact1);
        service.addContact(contact2);
        service.addToGroup("family", "1");
        service.addToGroup("family", "2");

        service.deleteContact("1");
        Set<Contact> familyGroup = service.getGroupContacts("family");
        assertEquals(1, familyGroup.size());
        assertTrue(familyGroup.contains(contact2));
    }

    @Test
    void testInvalidSearchType() {
        assertThrows(IllegalArgumentException.class, () -> 
            service.searchContacts("test", null));
    }

    @Test
    void testInvalidSortType() {
        assertThrows(IllegalArgumentException.class, () -> 
            service.getSortedContacts(null));
    }
}