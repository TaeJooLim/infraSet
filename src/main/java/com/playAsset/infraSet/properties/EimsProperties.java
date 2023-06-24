package com.playAsset.infraSet.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @Desc Properties Read
 * 1. properties 파일들은 src/main/resources 아래 경로에 존재해야한다.
 * 2. @SpringBootApplication 어노테이션을 통해 기본적인 설정을 선언할 때, 가지고 올 properties 파일들을 작성한다. (InfraSetApplication.java)
 * 4. ConfigurationProperties(prefix = "") .으로 구분되는 앞부분을 생략
 * 5. prefix.프로퍼티명과 field에 작성한 프로퍼티명(Key)이 일치하면 자동으로 mapping된다.
 * 6. Class파일에 @Autowired를 통해 인스턴스를 생성하고 getter를 사용해서 Value를 가져온다.
 */
@Component
@Data
@ConfigurationProperties(prefix = "eims")
public class EimsProperties {

    /* STATIC */
    public String EIMS_SIGN_PERIOD = ".";
    public String EIMS_SIGN_SEMICOLON = ";";
    public String EIMS_SIGN_SLASH = "/";
    public String EIMS_SIGN_ONBRACE = "{";
    public String EIMS_SIGN_OFFBRACE = "}";

    public String EIMS_EXTENSION_JAVA = ".java";

    public String EIMS_ENTITY_SPACE = " ";
    public String EIMS_ENTITY_PACAKGE = "package";
    public String EIMS_ENTITY_IMPORT = "import";
    public String EIMS_ENTITY_CLASS = "class";
    public String EIMS_ENTITY_PUBLIC = "public";
    public String EIMS_ENTITY_PRIVATE = "private";

    public String EIMS_ANNOTATION_LOMBOK = "@Data";
    public String EIMS_IMPORT_LOMBOK = "lombok.Data";


    /* EIMS */
    private String path;
}
