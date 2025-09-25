package practice.design.structural.adapter.notifier;

import practice.design.structural.adapter.lib.slack.SlackClient;

public class SlackNotifier implements Notifier {
    private static final SlackClient slackClient = new SlackClient();
    
    @Override
    public void send(String consumer, String content) {
        slackClient.postMessage(consumer, "[NOTIFY] " + content);
    }
}
