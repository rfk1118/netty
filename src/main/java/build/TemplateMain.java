package build;

public class TemplateMain {

    public static void main(String[] args) {
        TemplateMessageBuilder builder = TemplateMessageBuilder.builder();
        builder.setHeadName("renfakai")
                .setHeadBody("renfakai")
                .setActionUrl("www.58.com")
                .setActionLocation("left")
                .setAuthCode("auth01")
                .setTemplateId("TemplateIdaaaa01")
                .addItem(new Body.Item("item01", "item01"))
                .addItem(new Body.Item("item02", "item02"))
                .addItem(new Body.Item("item03", "item03"));
        TemplateMessage build = builder.build();
    }
}
