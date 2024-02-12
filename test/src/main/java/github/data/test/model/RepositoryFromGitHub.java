package github.data.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@JsonIgnoreProperties
public class RepositoryFromGitHub {
    private String name;
    private List<Branch> branches;
    @JsonIgnore
    private boolean fork;
    @JsonProperty("ownerLogin")
    private String ownerLogin;

    @JsonProperty("owner")
    private void unpackOwner(Map<String, Object> owner) {
        this.ownerLogin = owner != null ? (String) owner.get("login") : null;
    }
}
