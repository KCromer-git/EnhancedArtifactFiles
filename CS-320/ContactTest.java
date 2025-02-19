import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactTest{
    @Test
    public void testContactConstructorValidData(){
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        assertAll("constructor",
            () -> assertEquals("1", contact.getContactId()),
            () -> assertEquals("John", contact.getFirstName()),
            () -> assertEquals("Doe", contact.getLastName()),
            () -> assertEquals("1234567890", contact.getPhone()),
            () -> assertEquals("123 Main St", contact.getAddress())
        );
    }

    @Test
    public void testContactConstructorValidId(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Contact(null, "John", "Doe", "1234567890", "123 Main St"));
        assertEquals("Invalid contact ID", exception.getMessage());
    }

    @Test
    public void testContactConstructorLongFirstName() {
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        new Contact("1", "Johnathan", "Doe", "1234567890", "123 Main St"));
    assertEquals("Invalid first name", exception.getMessage());
    }

    @Test
    public void testContactConstructorLongLastName(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Contact("1", "John", "DoeDoeDoe", "1234567890", "123 Main St" ));
        assertEquals("Invalid last name", exception.getMessage());
    }

    @Test
    public void testContactConstructorInvalidPhoneLength() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Contact("1", "John", "Doe", "12345", "123 Main St"));
        assertEquals("Invalid phone number", exception.getMessage());
    }

    @Test
    public void testContactConstructorNonNumericPhone() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Contact("1", "John", "Doe", "12345abcde", "123 Main St"));
        assertEquals("Invalid phone number", exception.getMessage());
    }

    @Test
    public void testContactConstructorLongAddress() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            new Contact("1", "John", "Doe", "1234567890", "123 Main Street, Anytown, Anystate, 12345-6789"));
        assertEquals("Invalid address", exception.getMessage());
    }


    //updating tests
    @Test
    public void testUpdateFirstNameValid(){
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        contact.setFirstName("Jane");
        assertEquals("Jane", contact.getFirstName());
    }

    @Test
    public void testUpdateFirstNameInvalid(){
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            contact.setFirstName("JonathanTooLong"));
        assertEquals("Invalid first name", exception.getMessage());
    }

    @Test
    public void testUpdateLastNameValid() {
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        contact.setLastName("Smith");
        assertEquals("Smith", contact.getLastName());
    }

    @Test
    public void testUpdateLastNameInvalid() {
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            contact.setLastName("SmithsonianTooLong"));
        assertEquals("Invalid last name", exception.getMessage());
    }

    @Test
    public void testUpdatePhoneValid() {
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        contact.setPhone("0987654321");
        assertEquals("0987654321", contact.getPhone());
    }

    @Test
    public void testUpdatePhoneInvalidLength() {
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            contact.setPhone("123"));
        assertEquals("Invalid phone number", exception.getMessage());
    }

    @Test
    public void testUpdatePhoneInvalidCharacters() {
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            contact.setPhone("abcdefghij"));
        assertEquals("Invalid phone number", exception.getMessage());
    }

    @Test
    public void testUpdateAddressValid() {
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        contact.setAddress("456 Elm St");
        assertEquals("456 Elm St", contact.getAddress());
    }

    @Test
    public void testUpdateAddressInvalid() {
        Contact contact = new Contact("1", "John", "Doe", "1234567890", "123 Main St");
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            contact.setAddress("123 Elm Street, Anytown, Anystate, 12345-6789, United States of America"));
        assertEquals("Invalid address", exception.getMessage());
    }



}
