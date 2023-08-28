package Utility.others;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String id;
    private String name;
    private String year;

    public User(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public String getYear(){
        return year;
    }
}
