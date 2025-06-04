package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.EmployeeDAO;
import employeeDev.src.mappers.EmployeeMapper;
import java.time.LocalDate;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;
;

public class DriverDL extends EmployeeDL {
    private LicenseType licenseType;
    private boolean isAvailableToDrive;

    public DriverDL(String id, String password, String fullName, LocalDate workStartingDate, int wage, char wageTypeChar,
    int yearlySickDays, int yearlyDaysOff, Site site, String phoneNumber, LicenseType licenseType, RoleDL driverRole, boolean isAvailableToDrive) {
        super(id, password, fullName, workStartingDate, wage, wageTypeChar, yearlySickDays, yearlyDaysOff, site, phoneNumber);
        this.licenseType = licenseType;
        this.isAvailableToDrive = isAvailableToDrive;
        this.addRole(driverRole);
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public boolean isLicensed(LicenseType licenseType) {
        return this.licenseType.equals(licenseType);
    }

    public boolean isAvailableToDrive() {
        return isAvailableToDrive;
    }

    public void setAvailableToDrive(boolean isAvailableToDrive) {
        this.isAvailableToDrive = isAvailableToDrive;
    }

    @Override
    public boolean isDriver() {
        return true;
    }

    @Override
    public void insertIntoDB() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.instertDriver(EmployeeMapper.toDriverDTO(this));
    }

    @Override
    public void updateInDB() {
        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.updateDriver(EmployeeMapper.toDriverDTO(this));
    }

    
}
