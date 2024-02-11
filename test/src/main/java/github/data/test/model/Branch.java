package github.data.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties
public class Branch {
    private String name;

    public Branch(String name, String lastCommitSha) {

    }

    @JsonProperty("lastCommitSha")
    public String getLastCommitSha() {
        return commit != null ? commit.getSha() : null;
    }

    @JsonIgnore
    private Commit commit;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Commit {
        private String sha;

        public String getSha() {
            return sha;
        }
    }

}
