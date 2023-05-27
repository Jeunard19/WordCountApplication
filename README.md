# Java Word Frequency Analyzer

This Java project provides an implementation of two interfaces, `WordFrequency` and `WordFrequencyAnalyzer`, to analyze and calculate word frequencies in a given text.

## Interfaces

### `WordFrequency`

This interface defines the following methods:

```java
String getWord();
int getFrequency();
```
- `getWord()`: Returns the word associated with the object that implements WordFrequency.
- `getFrequency()`: Returns the frequency of the word associated with the object that implements WordFrequency.

### WordFrequencyAnalyzer

This interface provides methods to calculate word frequencies and retrieve information from a given text:

```java
int calculateHighestFrequency(String text);
int calculateFrequencyForWord(String text, String word);
List<WordFrequency> calculateMostFrequentNWords(String text, int n);
```

- `calculateHighestFrequency(String text)`: Calculates and returns the highest frequency of any word in the given text.
- `calculateFrequencyForWord(String text, String word)`: Calculates and returns the frequency of a specific word in the given text.
- `calculateMostFrequentNWords(String text, int n)`: Calculates and returns a list of n most frequent words as WordFrequency objects in the given text.

### REST API Implementation

The methods of the WordFrequencyAnalyzer interface have been implemented as REST APIs using Jakarta EE and a GlassFish server. The endpoints for these APIs are as follows:
- `GET /api/wordfrequency/highest-frequency?text={text}`: Calculates and returns the highest frequency of any word in the provided text.
- `GET /api/wordfrequency/frequency-word?text={text}&word={word}`: Calculates and returns the frequency of a specific word in the given text.
- `GET /api/wordfrequency/most-frequent-nwords?text={text}&n={n}`: Calculates and returns a list of n most frequent words as WordFrequency objects in the given text.

### Usage
1. Clone the repository to your local machine.
2. Import the project into your preferred Java development environment.
3. Deploy the project on a GlassFish server.
4. Access the APIs using the provided endpoints.

Here's an example of how to use the REST APIs:







