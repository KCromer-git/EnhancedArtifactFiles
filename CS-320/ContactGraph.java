class ContactGraph {
    private Map<String, Set<String>> groupContacts;
    private Map<String, Set<String>> contactGroups;

    public ContactGraph() {
        this.groupContacts = new HashMap<>();
        this.contactGroups = new HashMap<>();
    }

    public void addToGroup(String groupId, String contactId) {
        groupContacts.computeIfAbsent(groupId, k -> new HashSet<>()).add(contactId);
        contactGroups.computeIfAbsent(contactId, k -> new HashSet<>()).add(groupId);
    }

    public void removeContact(String contactId) {
        Set<String> groups = contactGroups.remove(contactId);
        if (groups != null) {
            groups.forEach(groupId -> groupContacts.get(groupId).remove(contactId));
        }
    }

    public Set<String> getGroupContacts(String groupId) {
        return new HashSet<>(groupContacts.getOrDefault(groupId, new HashSet<>()));
    }

    public Set<String> getContactGroups(String contactId) {
        return new HashSet<>(contactGroups.getOrDefault(contactId, new HashSet<>()));
    }
}