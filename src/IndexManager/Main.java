package IndexManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MangaCRUD mangaCRUD = new MangaCRUD();

        while (true) {
            System.out.println("Escolha a opcao desejada:");
            System.out.println("1 - Adicionar Manga");
            System.out.println("2 - Listar Mangas");
            System.out.println("3 - Atualizar Manga");
            System.out.println("4 - Excluir Manga");
            System.out.println("5 - Buscar Manga por Titulo");
            System.out.println("6 - Sair");
            System.out.print("Opcao: ");

            String opcao = sc.nextLine();

            switch (opcao) {
                case "1":
                    int isbn = lerInteiro(sc, "ISBN: ");
                    System.out.print("Escreva o Titulo: ");
                    String titulo = sc.nextLine();
                    int anoIni = lerInteiro(sc, "Escreva o Ano de Inicio: ");
                    int anoFim = lerInteiro(sc, "Escreva o Ano de Fim: ");
                    System.out.print("Escreva o Autor: ");
                    String autor = sc.nextLine();
                    System.out.print("Escreva o Genero: ");
                    String genero = sc.nextLine();
                    System.out.print("Escreva a Revista: ");
                    String revista = sc.nextLine();
                    System.out.print("Escreva a Editora: ");
                    String editora = sc.nextLine();
                    int anoEdi = lerInteiro(sc, "Escreva o Ano da Edicao: ");
                    int quantVolAdq = lerInteiro(sc, "Escreva a Quantidade de Volumes Adquiridos: ");

                    Manga novoManga = new Manga(isbn, titulo, anoIni, anoFim, autor, genero, revista, editora, anoEdi, quantVolAdq);
                    mangaCRUD.adicionarManga(novoManga);
                    break;

                case "2":
                    List<Manga> listaMangas = mangaCRUD.listarMangas();
                    for (Manga manga : listaMangas) {
                        System.out.println(manga);
                    }
                    break;

                case "3":
                    int isbnAtualizar = lerInteiro(sc, "ISBN do Manga a ser atualizado: ");
                    Manga mangaExistente = mangaCRUD.buscarPorISBN(isbnAtualizar);

                    if (mangaExistente != null) {
                        System.out.print("Novo Titulo: ");
                        String novoTitulo = sc.nextLine();
                        int novoAnoIni = lerInteiro(sc, "Novo Ano de Inicio: ");
                        int novoAnoFim = lerInteiro(sc, "Novo Ano de Fim: ");
                        System.out.print("Novo Autor: ");
                        String novoAutor = sc.nextLine();
                        System.out.print("Novo Genero: ");
                        String novoGenero = sc.nextLine();
                        System.out.print("Nova Revista: ");
                        String novaRevista = sc.nextLine();
                        System.out.print("Nova Editora: ");
                        String novaEditora = sc.nextLine();
                        int novoAnoEdi = lerInteiro(sc, "Novo Ano da Edicao: ");
                        int novaQuantVolAdq = lerInteiro(sc, "Nova Quantidade de Volumes Adquiridos: ");

                        Manga mangaAtualizado = new Manga(isbnAtualizar, novoTitulo, novoAnoIni, novoAnoFim, novoAutor, novoGenero, novaRevista, novaEditora, novoAnoEdi, novaQuantVolAdq);
                        mangaCRUD.alterarManga(isbnAtualizar, mangaAtualizado);
                    } else {
                        System.out.println("Manga com ISBN " + isbnAtualizar + " não encontrado.");
                    }
                    break;

                case "4":
                    int idExcluir = lerInteiro(sc, "ISBN do Manga a ser excluído: ");
                    System.out.print("Tem certeza que deseja excluir esse manga? (Y/N): ");
                    String result = sc.nextLine();
                    if (result.equalsIgnoreCase("Y")) {
                        mangaCRUD.excluirManga(idExcluir);
                    }
                    break;

                case "5":
                    System.out.print("Titulo do Manga: ");
                    String tituloBuscar = sc.nextLine();
                    Manga manga = mangaCRUD.buscarPorTitulo(tituloBuscar);
                    if (manga != null) {
                        System.out.println("Manga encontrado: " + manga);
                    } else {
                        System.out.println("Manga não encontrado.");
                    }
                    break;

                case "6":
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Opcao invalida.");
                    break;
            }
        }
    }

    private static int lerInteiro(Scanner sc, String mensagem) {
        int numero = -1;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(mensagem);
            String input = sc.nextLine();

            try {
                numero = Integer.parseInt(input);
                isValid = true;  // Se a conversão for bem-sucedida, saia do loop
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número inteiro.");
            }
        }

        return numero;
    }
}

