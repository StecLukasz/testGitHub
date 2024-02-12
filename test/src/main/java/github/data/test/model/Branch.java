package github.data.test.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties
public class Branch {
    private String name;
    private String lastCommitSha;


    public Branch(String name, String lastCommitSha) {
        this.name = name;
        this.setLastCommitSha(lastCommitSha);
    }

    @JsonProperty("commit")
    private void unpackNestedCommit(Map<String, Object> commit) {
        this.lastCommitSha = commit != null ? (String) commit.get("sha") : null;
    }

}
