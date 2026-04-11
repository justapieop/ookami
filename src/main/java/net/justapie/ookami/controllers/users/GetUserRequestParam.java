package net.justapie.ookami.controllers.users;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class GetUserRequestParam {
    private String id;
    private String username;
}
