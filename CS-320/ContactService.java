import java.util.*;
import java.util.stream.Collectors;

public class ContactService {
    private final Map<String, Contact> contacts;
    private final ContactBST searchTree;
    private final ContactGraph contactGroups;

    public ContactService() {
        this.contacts = new HashMap<>();
        this.searchTree = new ContactBST();
        this.contactGroups = new ContactGraph();
    }

    // CRUD Operations with enhanced validation
    public void addContact(Contact contact) {
        if (contact == null || contacts.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("Invalid contact or ID already exists");
        }
        contacts.put(contact.getContactId(), contact);
        searchTree.insert(contact);
    }

    public void deleteContact(String contactId) {
        Contact contact = contacts.remove(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact does not exist");
        }
        searchTree.remove(contact);
        contactGroups.removeContact(contactId);
    }

    public void updateContact(String contactId, String firstName, String lastName, String phone, String address) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact does not exist");
        }

        // Remove from search tree before update
        searchTree.remove(contact);

        if (firstName != null) contact.setFirstName(firstName);
        if (lastName != null) contact.setLastName(lastName);
        if (phone != null) contact.setPhone(phone);
        if (address != null) contact.setAddress(address);

        // Re-insert updated contact
        searchTree.insert(contact);
    }

    public Contact getContact(String contactId) {
        return contacts.get(contactId);
    }

    // Enhanced search functionality
    public List<Contact> searchContacts(String query, SearchType type) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(contacts.values());
        }

        query = query.toLowerCase().trim();
        final String searchQuery = query;

        switch (type) {
            case NAME:
                return searchTree.searchByName(query);
            case PHONE:
                return contacts.values().stream()
                    .filter(c -> c.getPhone().contains(searchQuery))
                    .collect(Collectors.toList());
            case ADDRESS:
                return contacts.values().stream()
                    .filter(c -> c.getAddress().toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Invalid search type");
        }
    }

    // Sort contacts using various algorithms
    public List<Contact> getSortedContacts(SortType type) {
        List<Contact> contactList = new ArrayList<>(contacts.values());
        
        switch (type) {
            case QUICKSORT:
                return quickSort(contactList);
            case MERGESORT:
                return mergeSort(contactList);
            default:
                throw new IllegalArgumentException("Invalid sort type");
        }
    }

    // Contact grouping functionality
    public void addToGroup(String groupId, String contactId) {
        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact does not exist");
        }
        contactGroups.addToGroup(groupId, contactId);
    }

    public Set<Contact> getGroupContacts(String groupId) {
        return contactGroups.getGroupContacts(groupId).stream()
            .map(contacts::get)
            .collect(Collectors.toSet());
    }

    // Helper enum classes for enhanced functionality
    public enum SearchType {
        NAME, PHONE, ADDRESS
    }

    public enum SortType {
        QUICKSORT, MERGESORT
    }

    private List<Contact> quickSort(List<Contact> contacts) {
    if (contacts.size() <= 1) return contacts;
    quickSortHelper(contacts, 0, contacts.size() - 1);
    return contacts;
}

private void quickSortHelper(List<Contact> contacts, int low, int high) {
    if (low < high) {
        int pi = partition(contacts, low, high);
        quickSortHelper(contacts, low, pi - 1);
        quickSortHelper(contacts, pi + 1, high);
    }
}

private int partition(List<Contact> contacts, int low, int high) {
    Contact pivot = contacts.get(high);
    int i = (low - 1);
    
    for (int j = low; j < high; j++) {
        if (compareContacts(contacts.get(j), pivot) <= 0) {
            i++;
            swap(contacts, i, j);
        }
    }
    swap(contacts, i + 1, high);
    return i + 1;
}

private List<Contact> mergeSort(List<Contact> contacts) {
    if (contacts.size() <= 1) return contacts;
    
    int mid = contacts.size() / 2;
    List<Contact> left = mergeSort(new ArrayList<>(contacts.subList(0, mid)));
    List<Contact> right = mergeSort(new ArrayList<>(contacts.subList(mid, contacts.size())));
    
    return merge(left, right);
}

private List<Contact> merge(List<Contact> left, List<Contact> right) {
    List<Contact> result = new ArrayList<>();
    int leftIndex = 0, rightIndex = 0;
    
    while (leftIndex < left.size() && rightIndex < right.size()) {
        if (compareContacts(left.get(leftIndex), right.get(rightIndex)) <= 0) {
            result.add(left.get(leftIndex++));
        } else {
            result.add(right.get(rightIndex++));
        }
    }
    
    result.addAll(left.subList(leftIndex, left.size()));
    result.addAll(right.subList(rightIndex, right.size()));
    return result;
}

private void swap(List<Contact> contacts, int i, int j) {
    Contact temp = contacts.get(i);
    contacts.set(i, contacts.get(j));
    contacts.set(j, temp);
}

private int compareContacts(Contact c1, Contact c2) {
    // Primary sort by last name, secondary by first name
    int lastNameCompare = c1.getLastName().compareToIgnoreCase(c2.getLastName());
    if (lastNameCompare != 0) return lastNameCompare;
    return c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
}
}
