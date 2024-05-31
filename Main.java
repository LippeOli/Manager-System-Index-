

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // Criar um novo usuário
        userDAO.addUser("John Doe", "john.doe@example.com");

        // Listar todos os usuários
        userDAO.getAllUsers().forEach(System.out::println);

        // Atualizar um usuário
        userDAO.updateUser(1, "Jane Doe", "jane.doe@example.com");

        // Deletar um usuário
        userDAO.deleteUser(1);
    }
}
