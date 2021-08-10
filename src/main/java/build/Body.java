package build;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * 模版消息整体
 */
@Data
public class Body {

    private List<Item> allItem;

    public Body(){
        allItem = new LinkedList<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        private String bodyName;

        private String bodyValue;
    }
}
