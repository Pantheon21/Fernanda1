import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsultaApp {
    public static void main(String[] args) {
        try {
            Connection conexao = ConexaoBD.obterConexao();
            Scanner scanner = new Scanner(System.in);

            // Exemplo: Listar todos os médicos
            listarMedicos(conexao);

            // Exemplo: Marcar uma consulta
            marcarConsulta(conexao, scanner);

            // Exemplo: Listar consultas de um paciente
            listarConsultasPaciente(conexao, scanner);

            // Fechar recursos
            scanner.close();
            conexao.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listarMedicos(Connection conexao) throws SQLException {
        String sql = "SELECT id, nome, especialidade FROM Medicos";
        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Lista de Médicos:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ": " + rs.getString("nome") +
                        " - " + rs.getString("especialidade"));
            }
            System.out.println();
        }
    }

    private static void marcarConsulta(Connection conexao, Scanner scanner) throws SQLException {
        System.out.println("Digite o ID do paciente: ");
        int idPaciente = scanner.nextInt();
        System.out.println("Digite o ID do médico: ");
        int idMedico = scanner.nextInt();
        System.out.println("Digite a data e hora da consulta (YYYY-MM-DD HH:MM): ");
        scanner.nextLine(); // Consumir a quebra de linha
        String dataHora = scanner.nextLine();

        String sql = "INSERT INTO Consultas (id_paciente, id_medico, data_hora, status) " +
                     "VALUES (?, ?, ?, 'Marcada')";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            stmt.setInt(2, idMedico);
            stmt.setString(3, dataHora);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Consulta marcada com sucesso!");
            } else {
                System.out.println("Falha ao marcar a consulta.");
            }
        }
    }

    private static void listarConsultasPaciente(Connection conexao, Scanner scanner) throws SQLException {
        System.out.println("Digite o ID do paciente: ");
        int idPaciente = scanner.nextInt();

        String sql = "SELECT c.id, m.nome as nome_medico, c.data_hora, c.status " +
                     "FROM Consultas c " +
                     "JOIN Medicos m ON c.id_medico = m.id " +
                     "WHERE c.id_paciente = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Consultas do Paciente:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                            ", Médico: " + rs.getString("nome_medico") +
                            ", Data e Hora: " + rs.getString("data_hora") +
                            ", Status: " + rs.getString("status"));
                }
                System.out.println();
            }
        }
    }
}
