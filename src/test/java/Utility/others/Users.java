package Utility.others;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    private String id;
    private String first_name;
    private String last_name;

    public Users(String first_name, String last_name){

        this.first_name = first_name;
        this.last_name = last_name;
    }
}
