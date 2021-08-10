package build;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TemplateMessage {

    private Header header;

    private Body body;

    private Action action;

    private Common common;

}
