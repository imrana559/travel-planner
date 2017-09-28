package ba.unsa.pmf.planerputovanja;

public class Galerija {
    private int id;
    private String ime;
    private String opis;
    private byte[] slika;

    public Galerija(String name, String price, byte[] image, int id) {
        this.ime = name;
        this.opis = price;
        this.slika = image;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return ime;
    }

    public void setName(String name) {
        this.ime = name;
    }

    public String getPrice() {
        return opis;
    }

    public void setPrice(String price) {
        this.opis = price;
    }

    public byte[] getImage() {
        return slika;
    }

    public void setImage(byte[] image) {
        this.slika = image;
    }
}
