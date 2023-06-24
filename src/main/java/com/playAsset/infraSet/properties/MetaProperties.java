package com.playAsset.infraSet.properties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@ConfigurationProperties(prefix = "meta")
public class MetaProperties {

    /* META */
    private String dataType;
    private String Type;
    private String DDLCreateTable;
    private String oneLineQuery1;
    private String oneLineQuery2;
    private String oneLineQueryPK;

    /* Getter, Setter */
    public List<String> getDataTypeList() {
        return Arrays.asList(this.getDataType().split(","));
    }
    public Set<String> getDataTypeSet() {
        Set<String> set = new HashSet<>();
        this.getDataTypeList().forEach(str -> {
            set.add(str);
        });
        return set;
    }
    public List<String> getTypeList() {
        return Arrays.asList(this.getType().split(","));
    }
    public Set<String> getTypeSet() {
        Set<String> set = new HashSet<>();
        this.getTypeList().forEach(str -> {
            set.add(str);
        });
        return set;
    }
}