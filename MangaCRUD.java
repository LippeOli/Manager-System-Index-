package IndexManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MangaCRUD {
    private static final String FILE_NAME = "mangas.txt";


    public void addManga(int id, String titulo, int anoIni, String genero ) {
        Manga manga = new Manga(id, titulo, anoIni, genero);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(manga.toString());
            writer.newLine();
            System.out.println("Manga criado com sucesso: " + manga);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listarMangas(){
        List<Manga> mangas = carregarMangas();
        if (mangas.isEmpty()) {
            System.out.println("Nenhum manga cadastrado.");
        } else {
            for (Manga manga : mangas) {
                System.out.println(manga);
            }
        }
    }

    private List<Manga> carregarMangas(){
        List<Manga> mangas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                mangas.add(Manga.fromString(linha));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mangas;
    }

    public void alterarMangas(int id, String novoTitulo, int novoAnoIni, String novoGenero) {
        List<Manga> mangas = carregarMangas();
        for (Manga manga : mangas) {
            if (manga.getId() == id) {
                manga.setTitulo(novoTitulo);
                manga.setAnoIni(novoAnoIni);
                manga.setGenero(novoGenero);
                break;
            }
        }
        salvarMangas(mangas);
    }

    public void salvarMangas(List<Manga> mangas){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Manga manga : mangas) {
                writer.write(manga.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void excluirManga(int id){
        List<Manga> mangas = carregarMangas();
        mangas.removeIf(manga -> manga.getId() == id);
        salvarMangas(mangas);
    }


}
