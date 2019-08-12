public class Zech {

    private String name;
    private String number;

    public Zech(final String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public String getNumber() {
        return this.number;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void main(String args[]) {

        Zech z1 = new Zech("J", "010");
       Zech adfsdafdsa = changeName(z1, "F");
       adfsdafdsa;



    }

    public Zech changeName(Zech z, String name) {

        Zech z2 = new Zech(name, z.getNumber());

        return z2;
    }
}
