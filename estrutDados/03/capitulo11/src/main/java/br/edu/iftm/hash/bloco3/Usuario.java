package br.edu.iftm.hash.bloco3;

/**
 * Classe Usuario para representar um usuário autenticado
 */
public class Usuario {
    private final long id;
    private final String perfil;

    public Usuario(long id, String perfil) {
        this.id = id;
        this.perfil = perfil;
    }

    public long getId() {
        return id;
    }

    public String getPerfil() {
        return perfil;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", perfil='" + perfil + '\'' +
                '}';
    }
}
