package practice.design.structural.adapter.solution.lib.slack;

public class SlackClient {
    public void postMessage(String channelId, String text) {
        System.out.println("[Slack] #" + channelId + " -> " + text);
    }
}
