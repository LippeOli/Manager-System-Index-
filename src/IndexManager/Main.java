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
                    System.out.print("ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    System.out.print("Escreva o Titulo: ");
                    String titulo = sc.nextLine();
                    System.out.print("Escreva o Ano de Inicio: ");
                    int anoIni = Integer.parseInt(sc.nextLine());
                    System.out.print("Escreva o Genero: ");
                    String genero = sc.nextLine();

                    mangaCRUD.addManga(id, titulo, anoIni, genero);
                    break;

                case "2":
                    mangaCRUD.listarMangas();
                    break;

                case "3":
                    System.out.print("ID do Manga a ser atualizado: ");
                    int idAtualizar = Integer.parseInt(sc.nextLine());
                    System.out.print("Novo Titulo: ");
                    String novoTitulo = sc.nextLine();
                    System.out.print("Novo Ano de Inicio: ");
                    int novoAnoIni = Integer.parseInt(sc.nextLine());
                    System.out.print("Novo Genero: ");
                    String novoGenero = sc.nextLine();
                    mangaCRUD.alterarMangas(idAtualizar, novoTitulo, novoAnoIni, novoGenero);
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
