import dto.AdminDTO;
import service.admin.AdminService;

public class main {
	public static void main (String[] args) {
		AdminDTO admin = new AdminDTO();
		admin.setId("ADM00001");
        admin.setName("Alex");
        admin.setPhoneNumber("601120711043");
        admin.setAddressId("ADR00001"); // Fixed key
        admin.setEmailAddress("thoalex2728@gmail.com"); // Fixed key
        admin.setPassword("1"); // Added password
        admin.setStatus(true);
        
        AdminService.createAdmin(admin);
	}
}
