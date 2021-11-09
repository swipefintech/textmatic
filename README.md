# Textmatic

If you ever wanted a tool to simply push the SMS (or text messages) from your phone to somewhere remote, this is it.
This app matches all incoming and/or outgoing text messages against set rules and sends them over to webhook that you define.

If you find this app useful, send over your hugs :hugs: to [Float](https://float.africa/).

### Usage

The app sends payload similar to below to the remote webhook:

```json
{
    "direction": "incoming",
    "participant": "+919876543210",
    "content": "Your OTP for login is 123456.",
    "date": 1609459200000
}
```

You can use services like [Pipedream](https://pipedream.com/) to process these payloads e.g., sending over to a Slack channel using below code:

```js
async (event, steps) => {
  const color = event.body.direction === "incoming" ? "#0099e5" : "#34bf49";
  const icon = event.body.direction === "incoming" ? ":inbox_tray:" : ":outbox_tray:";
  return require('axios')
    .post('https://hooks.slack.com/services/<your-webhook-url>', {
      text: `New ${event.body.direction} ${icon} message captured.`,
        attachments: [{
        author_name: event.body.participant,
        color,
        footer: 'Textmatic',
        text: event.body.content,
        ts: event.body.date / 1000,
      }],
    });
}
```

### Download

You can download the latest version from [Releases](https://github.com/swipefintech/textmatic/releases) page.

### License

See [LICENSE](LICENSE) file.
