## Model
 
The app uses **llama3.2** running locally through **Ollama**. Spring AI connects to it through Ollama's OpenAI-compatible API, so the same code also works with cloud models on OpenRouter by changing only `.env`.

## Run
 
```
ollama pull llama3.2
```
 
Create `.env` in the project root:
```
OPENROUTER_API_KEY=[anything]
OPENROUTER_MODEL=llama3.2
OPENROUTER_BASE_URL=http://localhost:11434/v1
```
 
Start the app and open http://localhost:8080.

## Working example
<img width="1688" height="880" alt="Screenshot 2026-10-30 001759" src="https://github.com/user-attachments/assets/6db81fa0-4389-4a4c-8aa6-c872b2a81f6e" />
