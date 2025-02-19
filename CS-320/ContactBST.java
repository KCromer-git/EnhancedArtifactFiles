class ContactBST {
    private Node root;

    private class Node {
        Contact contact;
        Node left, right;

        Node(Contact contact) {
            this.contact = contact;
        }
    }

    public void insert(Contact contact) {
        root = insertRec(root, contact);
    }

    private Node insertRec(Node root, Contact contact) {
        if (root == null) {
            return new Node(contact);
        }

        int cmp = compareContacts(contact, root.contact);
        if (cmp < 0) {
            root.left = insertRec(root.left, contact);
        } else if (cmp > 0) {
            root.right = insertRec(root.right, contact);
        }

        return root;
    }

    public void remove(Contact contact) {
        root = removeRec(root, contact);
    }

    private Node removeRec(Node root, Contact contact) {
        if (root == null) {
            return null;
        }

        int cmp = compareContacts(contact, root.contact);
        if (cmp < 0) {
            root.left = removeRec(root.left, contact);
        } else if (cmp > 0) {
            root.right = removeRec(root.right, contact);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            root.contact = minValue(root.right);
            root.right = removeRec(root.right, root.contact);
        }

        return root;
    }

    private Contact minValue(Node root) {
        Contact minv = root.contact;
        while (root.left != null) {
            minv = root.left.contact;
            root = root.left;
        }
        return minv;
    }

    public List<Contact> searchByName(String query) {
        List<Contact> results = new ArrayList<>();
        searchByNameRec(root, query.toLowerCase(), results);
        return results;
    }

    private void searchByNameRec(Node root, String query, List<Contact> results) {
        if (root == null) {
            return;
        }

        String fullName = (root.contact.getFirstName() + " " + root.contact.getLastName()).toLowerCase();
        if (fullName.contains(query)) {
            results.add(root.contact);
        }

        searchByNameRec(root.left, query, results);
        searchByNameRec(root.right, query, results);
    }

    private int compareContacts(Contact c1, Contact c2) {
        String name1 = c1.getLastName() + c1.getFirstName();
        String name2 = c2.getLastName() + c2.getFirstName();
        return name1.compareToIgnoreCase(name2);
    }
}