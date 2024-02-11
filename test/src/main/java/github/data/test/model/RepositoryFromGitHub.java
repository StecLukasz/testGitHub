package github.data.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonIgnoreProperties
public class RepositoryFromGitHub {
    private String name;

    @JsonIgnore // Zignoruj zagnieżdżony obiekt 'owner' przy serializacji JSON
    private Owner owner;

    private List<Branch> branches;

    @JsonProperty("ownerLogin")
    public String getOwnerLogin() {
        return owner != null ? owner.getLogin() : null;
    }

    public boolean isFork() {
        return false;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Owner {
        private String login;

        public String getLogin() {
            return login;
        }
    }
}
