package IndexManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MangaCRUD mangaCRUD = new MangaCRUD();
        Scanner sc = new Scanner(System.in);

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

                    System.out.print("ISBN: ");
                    int isbn = Integer.parseInt(sc.nextLine());
                    System.out.print("Escreva o Titulo: ");
                    String titulo = sc.nextLine();
                    System.out.print("Escreva o Ano de Inicio: ");
                    int anoIni = Integer.parseInt(sc.nextLine());
                    System.out.print("Escreva o Ano de Fim: ");
                    int anoFim = Integer.parseInt(sc.nextLine());
                    System.out.print("Escreva o Autor: ");
                    String autor = sc.nextLine();
                    System.out.print("Escreva o Genero: ");
                    String genero = sc.nextLine();
                    System.out.print("Escreva a Revista: ");
                    String revista = sc.nextLine();
                    System.out.print("Escreva a Editora: ");
                    String editora = sc.nextLine();
                    System.out.print("Escreva o Ano da Edicao: ");
                    int anoEdi = Integer.parseInt(sc.nextLine());
                    System.out.print("Escreva a Quantidade de Volumes Adquiridos: ");
                    int quantVolAdq = Integer.parseInt(sc.nextLine());

                    mangaCRUD.addManga(isbn, titulo, anoIni, anoFim, autor, genero, revista, editora, anoEdi, quantVolAdq);
                    break;

                case "2":
                    mangaCRUD.listarMangas();
                    break;

                case "3":
                    System.out.print("ISBN do Manga a ser atualizado: ");
                    int isbnAtualizar = Integer.parseInt(sc.nextLine());

                    Manga mangaExistente = mangaCRUD.buscarPorISBN(isbnAtualizar);

                    if(mangaExistente != null) {
                        System.out.print("Novo Titulo: ");
                        String novoTitulo = sc.nextLine();
                        System.out.print("Novo Ano de Inicio: ");
                        int novoAnoIni = Integer.parseInt(sc.nextLine());
                        System.out.print("Novo Ano de Fim: ");
                        int novoAnoFim = Integer.parseInt(sc.nextLine());
                        System.out.print("Novo Autor: ");
                        String novoAutor = sc.nextLine();
                        System.out.print("Novo Genero: ");
                        String novoGenero = sc.nextLine();
                        System.out.print("Nova Revista: ");
                        String novaRevista = sc.nextLine();
                        System.out.print("Nova Editora: ");
                        String novaEditora = sc.nextLine();
                        System.out.print("Novo Ano da Edicao: ");
                        int novoAnoEdi = Integer.parseInt(sc.nextLine());
                        System.out.print("Nova Quantidade de Volumes Adquiridos: ");
                        int novaQuantVolAdq = Integer.parseInt(sc.nextLine());

                        mangaCRUD.alterarMangas(isbnAtualizar, novoTitulo, novoAnoIni, novoAnoFim, novoAutor, novoGenero, novaRevista, novaEditora, novoAnoEdi, novaQuantVolAdq);
                    }else {
                        System.out.println("Manga com ISBN " + isbnAtualizar + " não encontrado.");
                    }
                    break;

                case "4":
                    System.out.print("ID do Manga a ser excluído: ");
                    int idExcluir = Integer.parseInt(sc.nextLine());

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
                        System.out.println("Manga nao encontrado.");
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
}
