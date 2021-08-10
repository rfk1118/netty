package build;

import java.util.LinkedList;
import java.util.List;

public class TemplateMessageBuilder {

    private String headName;

    private String headBody;

    private List<Body.Item> allItem;

    private String actionUrl;

    private String actionLocation;

    private String templateId;

    private String authCode;

    public TemplateMessageBuilder() {
        allItem = new LinkedList<>();
    }

    public TemplateMessageBuilder setHeadName(String headName) {
        this.headName = headName;
        return this;
    }

    public TemplateMessageBuilder setHeadBody(String headBody) {
        this.headBody = headBody;
        return this;
    }

    public TemplateMessageBuilder setAllItem(List<Body.Item> allItem) {
        this.allItem.addAll(allItem);
        return this;
    }

    public TemplateMessageBuilder addItem(Body.Item allItem) {
        this.allItem.add(allItem);
        return this;
    }

    public TemplateMessageBuilder setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
        return this;
    }

    public TemplateMessageBuilder setActionLocation(String actionLocation) {
        this.actionLocation = actionLocation;
        return this;
    }

    public TemplateMessageBuilder setTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public TemplateMessageBuilder setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    public static TemplateMessageBuilder builder(){
        return new TemplateMessageBuilder();
    }

    public TemplateMessage build() {
        if (allItem.isEmpty()) {
            throw new IllegalArgumentException("body item不能为空");
        }
        if (null == authCode || authCode.isEmpty()) {
            throw new IllegalArgumentException("安全码不能为空");
        }

        // todo 按照业务自己定义规则
        TemplateMessage result = new TemplateMessage();
        Header header = new Header();
        header.setHeadName(this.headName);
        header.setHeadBody(this.headBody);
        result.setHeader(header);

        Body body = new Body();
        body.setAllItem(this.allItem);
        result.setBody(body);

        Common common = new Common();
        common.setTemplateId(this.templateId);
        common.setAuthCode(this.authCode);
        result.setCommon(common);

        Action action = new Action();
        action.setActionUrl(this.actionUrl);
        action.setActionLocation(this.actionLocation);
        result.setAction(action);
        return result;
    }
}
