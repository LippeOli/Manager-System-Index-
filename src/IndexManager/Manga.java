package IndexManager;

public class Manga {
    private int isbn;
    private String titulo;
    private int anoIni;
    private int anoFim;
    private String autor;
    private String genero;
    private String revista;
    private String editora;
    private int anoEdi;
    private int quantVolAdq;

    public Manga(int isbn, String titulo, int anoIni, int anoFim, String autor, String genero, String revista, String editora, int anoEdi, int quantVolAdq) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.anoIni = anoIni;
        this.anoFim = anoFim;
        this.autor = autor;
        this.genero = genero;
        this.revista = revista;
        this.editora = editora;
        this.anoEdi = anoEdi;
        this.quantVolAdq = quantVolAdq;
    }

    public int getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAnoIni() {
        return anoIni;
    }

    public int getAnoFim() {
        return anoFim;
    }

    public String getAutor() {
        return autor;
    }

    public String getGenero() {
        return genero;
    }

    public String getRevista() {
        return revista;
    }

    public String getEditora() {
        return editora;
    }

    public int getAnoEdi() {
        return anoEdi;
    }

    public int getQuantVolAdq() {
        return quantVolAdq;
    }

    @Override
    public String toString() {
        return isbn + "," + titulo + "," + anoIni + "," + anoFim + "," + autor + "," + genero + "," + revista + "," + editora + "," + anoEdi + "," + quantVolAdq;
    }

    public static Manga fromString(String linha) {
        String[] parts = linha.split(",");
        int isbn = Integer.parseInt(parts[0]);
        String titulo = parts[1];
        int anoIni = Integer.parseInt(parts[2]);
        int anoFim = Integer.parseInt(parts[3]);
        String autor = parts[4];
        String genero = parts[5];
        String revista = parts[6];
        String editora = parts[7];
        int anoEdi = Integer.parseInt(parts[8]);
        int quantVolAdq = Integer.parseInt(parts[9]);
        return new Manga(isbn, titulo, anoIni, anoFim, autor, genero, revista, editora, anoEdi, quantVolAdq);
    }
}
