# Simulador de Sistema de Arquivos com Journaling (JFS)

**UNIVERSIDADE DE FORTALEZA** **CENTRO DE CIÊNCIAS TECNOLÓGICAS - CURSO: CIÊNCIA DA COMPUTAÇÃO** **Disciplina:** Sistemas Operacionais
**Autores:** João Paulo Del Vecchio e João Victor Araújo

**Palavras-chave:** Sistemas Operacionais. Sistema de Arquivos. Journaling. Write-Ahead Logging. Java.

---

## Resumo

Este trabalho propõe o desenvolvimento de um Simulador de Sistema de Arquivos com Journaling (JFS), implementado em Java como atividade prática da disciplina de Sistemas Operacionais. O simulador reproduz o funcionamento de um sistema de arquivos hierárquico baseado no modelo Write-Ahead Logging (WAL), no qual toda operação é registrada em um journal antes de ser efetivada. O sistema é persistido em um único arquivo binário (.jfs), oferece um shell interativo com comandos similares aos de sistemas Unix e implementa mecanismos de recuperação de falhas.

## Introdução

Os sistemas de arquivos são componentes fundamentais dos sistemas operacionais modernos, responsáveis por organizar, armazenar e recuperar dados em dispositivos de armazenamento. Entre as técnicas que garantem a integridade desses dados, o journaling se destaca por registrar previamente as intenções de escrita em um log especial, permitindo que o sistema se recupere de falhas sem corromper sua estrutura. Sistemas como **ext4** (Linux) e **NTFS** (Windows) utilizam journaling em produção, tornando o seu estudo essencial na formação de profissionais de Ciência da Computação.

## Metodologia

O simulador foi desenvolvido integralmente em Java, operando com uma arquitetura que separa a lógica de negócio da interface em dois pacotes: `core` (classes `FSNode`, `File`, `Directory`, `Journal`, `JournalEntry` e `FileSystemSimulator`) e `shell` (classe `Shell` para o terminal interativo). Todo o estado do sistema de arquivos virtual é serializado em um único arquivo binário (`.jfs`) via `ObjectOutputStream`. O modelo de journaling adotado foi o **Write-Ahead Logging (WAL)**: antes de cada operação, uma entrada com status `PENDING` é gravada no journal; após a conclusão bem-sucedida, é marcada como `COMMITTED`; em caso de falha, como `ABORTED`.

## Resultados e Discussão

O simulador foi submetido a testes funcionais cobrindo todas as operações implementadas. O shell interativo respondeu corretamente a todos os comandos, o journal registrou as operações com os status corretos e a persistência entre sessões foi validada. A funcionalidade de recuperação foi testada simulando uma interrupção artificial: na reinicialização, as entradas `PENDING` foram corretamente marcadas como `ABORTED`, demonstrando a eficácia do mecanismo WAL.

Abaixo, o shell interativo do simulador em execução:

 <img width="400" height="350" alt="{E10E967C-B244-4EB1-A108-47963DC40AEC}" src="https://cdn.discordapp.com/attachments/591034611480657931/1512332063011770388/preview.png?ex=6a23b47d&is=6a2262fd&hm=3e0078855af9075d447130581de4ad1047ebb53f73ddfbb9a05876855d7303e5&" />
## Conclusão

O desenvolvimento do simulador cumpriu seu objetivo pedagógico, ilustrando na prática os conceitos de journaling e Write-Ahead Logging. A separação entre camada de negócio (`core`) e interface (`shell`) garantiu organização e facilidade de manutenção. Como trabalhos futuros, sugere-se a implementação de permissões por usuário, links simbólicos e uma GUI para visualização em tempo real do journal.

## Referências

SILBERSCHATZ, A.; GALVIN, P. B.; GAGNE, G. Sistemas Operacionais com Java. 7. ed. Rio de Janeiro: Elsevier, 2008.

TANENBAUM, A. S. Sistemas Operacionais Modernos. 4. ed. São Paulo: Pearson, 2016.

---

## 💻 Como Executar o Projeto

**Pré-requisitos:**
* Java Development Kit (JDK) 17 ou superior instalado.
* Editor de código (ex: Visual Studio Code com extensão "Extension Pack for Java").

### 1. Preparação do Ambiente
1. Clone este repositório:
   ```bash
   git clone https://github.com/JoaoPauloDel/av3-Simulador-de-Sistema-de-Arquivos.git
   ```
2. Abra a pasta do projeto no **Terminal**:
   ```bash
   cd av3-Simulador-de-Sistema-de-Arquivos
   ```
3. Abra no **VS Code**:
   ```bash
   code .
   ```
4. Certifique-se de que os arquivos estão em `src/main/java/com/fssimulator/`.

### 2. Compilação e Execução (Terminal)

**Linux / macOS:**
```bash
chmod +x build.sh
./build.sh
```

**Windows:**
```bat
mkdir out\classes
dir /s /b src\main\java\*.java > out\sources.txt
javac -d out\classes @out\sources.txt
jar cfm out\fssimulator.jar out\MANIFEST.MF -C out\classes .
```

**Executar:**
```bash
java -jar out/fssimulator.jar         # Shell interativo
java -jar out/fssimulator.jar --demo  # Modo demo
```

**LINK:** https://github.com/JoaoPauloDel/av3-Simulador-de-Sistema-de-Arquivos
