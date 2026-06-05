# Simulador de Sistema de Arquivos com Journaling (JFS)

*Autor 1: Aluno 1.*
*Autor 2: Aluno 2.*

**Palavras-chave:** Sistemas Operacionais. Sistema de Arquivos. Journaling. Write-Ahead Logging. Java.

---

## Resumo

Este trabalho propõe o desenvolvimento de um Simulador de Sistema de Arquivos com Journaling (JFS), implementado em Java como atividade prática da disciplina de Sistemas Operacionais. O simulador reproduz o funcionamento de um sistema de arquivos hierárquico baseado no modelo Write-Ahead Logging (WAL), no qual toda operação é registrada em um journal antes de ser efetivada. O sistema é persistido em um único arquivo binário (.jfs), oferece um shell interativo com comandos similares aos de sistemas Unix e implementa mecanismos de recuperação de falhas.

---

## Introdução

Os sistemas de arquivos são componentes fundamentais dos sistemas operacionais modernos, responsáveis por organizar, armazenar e recuperar dados em dispositivos de armazenamento. Entre as técnicas que garantem a integridade desses dados, o journaling se destaca por registrar previamente as intenções de escrita em um log especial, permitindo que o sistema se recupere de falhas sem corromper sua estrutura.

Sistemas como **ext4** (Linux) e **NTFS** (Windows) utilizam journaling em produção. Este projeto propõe a implementação de um simulador educacional que reproduz, em Java, o comportamento de um sistema de arquivos com journaling completo.

---

## Metodologia

O simulador foi desenvolvido integralmente em **Java 17 LTS**, estruturado em dois pacotes:

- `core` — classes `FSNode`, `File`, `Directory`, `Journal`, `JournalEntry` e `FileSystemSimulator`
- `shell` — classe `Shell` para a interface interativa

Todo o estado é serializado em um único arquivo binário (`.jfs`) via `ObjectOutputStream`. O modelo de journaling adotado é o **Write-Ahead Logging (WAL)**:

```
[Operação solicitada]
       ↓
[1] journal.begin() → status: PENDING
       ↓
[2] save() — persiste PENDING no disco
       ↓
[3] Executa a operação
    ┌──────────────┐
  Sucesso        Falha
    ↓               ↓
COMMITTED       ABORTED
```

Na próxima abertura, entradas `PENDING` são detectadas por `recoverPending()` e marcadas como `ABORTED`.

---

## Resultados e Discussão

O simulador foi submetido a testes funcionais cobrindo todas as operações: criação e remoção de arquivos e diretórios, escrita e leitura de conteúdo, cópia e renomeação recursiva, e recuperação de journal.

```
fs> mkdir /documentos
[OK] Diretório criado: /documentos

fs> write /documentos/readme.txt Olá mundo!
[OK] Arquivo criado e escrito: /documentos/readme.txt

fs> journal
[JOURNAL] Log de operações (2 entrada(s)):
  #0001 CREATE_DIR   COMMITTED  path=/documentos
  #0002 WRITE_FILE   COMMITTED  path=/documentos/readme.txt
```

A separação entre o sistema virtual e o sistema hospedeiro foi confirmada — nenhum arquivo criado virtualmente apareceu no explorador de arquivos do sistema real.

---

## Conclusão

O desenvolvimento do simulador cumpriu seu objetivo pedagógico, ilustrando na prática os conceitos de journaling e Write-Ahead Logging. A separação entre camada de negócio (`core`) e interface (`shell`) garantiu organização e facilidade de manutenção. Como trabalhos futuros, sugere-se permissões por usuário, links simbólicos e uma GUI para visualização do journal.

---

## Referências

SILBERSCHATZ, A.; GALVIN, P. B.; GAGNE, G. *Sistemas Operacionais com Java*. 7. ed. Rio de Janeiro: Elsevier, 2008.

TANENBAUM, A. S. *Sistemas Operacionais Modernos*. 4. ed. São Paulo: Pearson, 2016.

---

## Instalação e Uso

### Pré-requisitos

- **Java JDK 17+** — verificar com `java -version`

### Compilação

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

### Execução

```bash
# Modo shell interativo
java -jar out/fssimulator.jar

# Modo demo (executa operações automaticamente e abre o shell)
java -jar out/fssimulator.jar --demo
```

### Comandos disponíveis

| Comando | Exemplo | Descrição |
|---------|---------|-----------|
| `touch <path>` | `touch /docs/file.txt` | Cria arquivo vazio |
| `write <path> <conteudo>` | `write /docs/file.txt Olá` | Grava conteúdo |
| `cat <path>` | `cat /docs/file.txt` | Lê conteúdo |
| `cp <src> <dst>` | `cp /docs/a.txt /bkp/a.txt` | Copia arquivo |
| `rm <path>` | `rm /docs/file.txt` | Remove arquivo |
| `mv <path> <novoNome>` | `mv /docs/file.txt novo.txt` | Renomeia arquivo |
| `mkdir <path>` | `mkdir /docs/sub` | Cria diretório |
| `rmdir <path>` | `rmdir /docs/sub` | Remove diretório |
| `mvdir <path> <novoNome>` | `mvdir /docs documentos` | Renomeia diretório |
| `cpdir <src> <dst>` | `cpdir /docs /bkp/docs` | Copia diretório |
| `ls [path]` | `ls /docs` | Lista conteúdo |
| `journal` | `journal` | Exibe o log WAL |
| `info` | `info` | Informações do FS |
| `exit` | `exit` | Salva e encerra |

---

## Autores

| Nome | Matrícula |
|------|-----------|
| João Victor Araújo | 2410386 |
| João Paulo Del Vecchio | 2413537 |

**Link do repositório:** https://github.com/JoaoPauloDel/av3-Simulador-de-Sistema-de-Arquivos
