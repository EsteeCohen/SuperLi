package presentationLayer;

public abstract class Form {
    private final String formName;

    public Form(String formName) {
        this.formName = formName;
        System.out.println("=== " + formName + " ===");
    }

    public String getFormName() {
        return formName;
    }
}
