package IndexManager;

public class Manga {

    private int id;
    private String titulo;
    private int anoIni;
    private String genero;

    public Manga(int id, String idade, int anoIni, String genero) {
        this.id = id;
        this.anoIni = anoIni;
        this.titulo = idade;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnoIni() {
        return anoIni;
    }
    public void setAnoIni(int anoIni) {
        this.anoIni = anoIni;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return id + "," + titulo + "," + anoIni + "," + genero;
    }

    public static Manga fromString(String mangaStr){
        String[] parts = mangaStr.split(",");
        return new Manga(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]), parts[3]);
    }


}
