# react-native-google-speech-to-text

## Getting started

`$ npm install react-native-google-speech-to-text --save`

### Mostly automatic installation

`$ react-native link react-native-google-speech-to-text`

## Usage

```javascript
import SpeechToText from 'react-native-google-speech-to-text';

// TODO: What to do with the module?
const speechToTextHandler = async () => {

    let speechToTextData = null;
        try {
            speechToTextData = await SpeechToText.startSpeech('Try saying something', 'en_IN');
            console.log('speechToTextData: ', speechToTextData);

        } catch (error) {
            console.log('error: ', error);
        }
}
```
