## OpenAI (ChatGPT) und Apache Kafka

![NebulIT Labs](/assets/banner.png)
Dieses POC Projekt verheiratet OpenAI mit Apache Kafka und ermöglicht Real Time Message filtering
über die KI.

Hierfür senden wir einen Prompt samt Record an die OpenAI API und verarbeiten das Ergebnis.

Fachlich verarbeiten wir Zahlungsdaten.

```
class Payment {
    var amount: BigDecimal = BigDecimal(0)
    var age = 16
}
```

Die KI soll auf Basis des Betrages und des Alters überprüfen, ob die Zahlung verdächtig ist.
Verdächtige Zahlungen werden anschließend direkt ausgefiltert und nicht verarbeitet.

In diesem Beispiel arbeiten wir direkt mit der Consumer API.
Weitere Beispiele die folgen sind:

- Kafka Streams mit Echtzeitverarbeitung
- KSQL + OpenAI
- Anreichern von Records um MetaDaten
- Rerouting von Records
- etc.

Um die den Testcase zu starten wird _Docker_ und ein OpenAI API Key benötigt.

Anschließend reicht es, den _OpenAiKafkaApplicationTests_ Testcase auszuführen.

Dieser:

Startet ein lokales Kafka
Sendet 10 Random Records
Records werden von OpenAI analysiert und in Echtzeit gefiltert.
Ergebnis der Filterung wird im Log ausgegeben.

Prompt für die Analyse findet sich in der Datei src/main/resources/application.properties:
```
openai.advice="you are a payment analyzing software. You will get requests in json containing age and amount. Return parseable JSON in the form : {\"suspicious\": true|false,\"reasoning\":\"<your reasoning>\"} if the payment is suspicious. Assume kids have the average amount of pocket money in germany. Give your reasoning in the 'reasoning' field"
```

OpenAI API Key als Environment Variable "TOKEN" übergeben (alternativ die application.properties direkt anpassen)

```
open-ai.token=${TOKEN}
```

Anregungen und Fragen gerne jederzeit unter https://www.nebulit.de
Die hier bereitgestellten Sourcen dienen lediglich der Fortbildung und Veranschaulichung.
Wir übernehmen keine Haftung für die korrekte Funktionsweise.

Martin Dilger
NebulIT GmbH
