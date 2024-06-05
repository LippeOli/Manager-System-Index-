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
            System.out.println("5 - Sair");
            System.out.print("Opcao: ");

            String opcao = sc.nextLine();

            switch (opcao) {
                case "1":
                    System.out.print("Escreva o Titulo: ");
                    String titulo = sc.nextLine();
                    System.out.print("Escreva o Ano de Inicio: ");
                    int anoIni = Integer.parseInt(sc.nextLine());
                    System.out.print("Escreva o Genero: ");
                    String genero = sc.nextLine();

                    mangaCRUD.addManga(titulo, anoIni, genero);
                    break;

                case "2":
                    mangaCRUD.listarMangas();
                    break;

//            case "3":
//                System.out.print("Escreva o ID do Manga a ser atualizado: ");
//                int idAtualizar = Integer.parseInt(sc.nextLine());
//                System.out.print("Escreva o novo Titulo: ");
//                String novoTitulo = sc.nextLine();
//                System.out.print("Escreva o novo Ano de Inicio: ");
//                int novoAnoIni = Integer.parseInt(sc.nextLine());
//                System.out.print("Escreva o novo Genero: ");
//                String novoGenero = sc.nextLine();
//
//                mangaCRUD.alterarManga(idAtualizar, novoTitulo, novoAnoIni, novoGenero);
//                break;

                case "4":
                    System.out.print("Escreva o ID do Manga a ser excluído: ");
                    int idExcluir = Integer.parseInt(sc.nextLine());

                    System.out.print("Tem certeza que deseja excluir esse manga? (Y/N)");
                    String result = sc.nextLine();
                    if (result.equalsIgnoreCase("Y")) {
                        mangaCRUD.excluirManga(idExcluir);
                        System.out.println("Manga excluído com sucesso.");
                    } else {
                        System.out.println("Exclusão cancelada.");
                    }

                    break;

                case "5":
                    System.out.println("Saindo...");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opcao invalida, tente novamente.");
            }


        }
    }
}
