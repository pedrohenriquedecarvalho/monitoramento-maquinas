# Relatório — Monitoramento de Máquinas (DevOps + Railway)

**Aluno:** ______________________  **Turma:** ________  **Data:** ____/____/______
**Repositório:** https://github.com/pedrohenriquedecarvalho/monitoramento-maquinas
**URL pública (Railway):** https://______________________.up.railway.app

## 1. Evidências (cole os prints em cada item)
| Item | Evidência | Onde tirar o print |
|---|---|---|
| a | Repositório Git | página inicial do repositório no GitHub (mostrando `src/`, `Dockerfile`, `README.md`, `.github/`) |
| b | Histórico de commits | GitHub → *Commits* (ou `git log --oneline --graph`) |
| c | Branches / Pull Requests | GitHub → *Branches* e *Pull requests* (`main`, `develop`, `feature/*`) |
| d | Testes automatizados | terminal com `mvn test` mostrando `Tests run: 15, Failures: 0` |
| e | Pipeline com sucesso | GitHub → *Actions* → execução verde |
| f | Thunder Client (GET, POST, PUT, DELETE) | importe `docs/thunder-client-monitoramento.json` e rode cada requisição (mostre método, URL, status e corpo) |
| g | Aplicação acessível | navegador/Thunder Client na URL pública da Railway |
| h | Endpoint `/status` | `GET https://<sua-url>/status` retornando 200 |
| i | Explicação oral | use o roteiro da seção 4 |

## 2. Perguntas

**a) Como o controle de versão contribui para a rastreabilidade do projeto?**
Cada alteração vira um commit com autor, data e mensagem, então dá para saber *quem* mudou *o quê* e *por quê*.
Com isso é possível comparar versões, voltar a um estado anterior quando algo quebra (`git revert`) e encontrar o
commit que introduziu um erro. Branches e pull requests ainda registram a revisão e a discussão de cada funcionalidade
antes de ela chegar à `main`.

**b) Qual é a importância dos testes automatizados em um processo de CI/CD?**
Eles validam o código a cada push sempre do mesmo jeito, sem depender de alguém lembrar de testar. Se um teste falha,
o pipeline fica vermelho e a versão com defeito não segue para o deploy. Isso reduz regressões, dá segurança para
mudar o código e encurta o tempo entre errar e descobrir o erro.

**c) Como o Docker ajuda a padronizar o ambiente da aplicação?**
O `Dockerfile` descreve o sistema, a versão do Java e o jeito de iniciar a aplicação. A mesma imagem roda igual no
computador do desenvolvedor, no CI e na Railway, o que elimina o "na minha máquina funciona". Também isola as
dependências e facilita escalar criando mais instâncias da mesma imagem.

**d) Quais etapas foram automatizadas no pipeline?**
O workflow `.github/workflows/pipeline.yml` é disparado a cada push e pull request e executa: (1) download do
código (`actions/checkout`); (2) configuração do Java 21 com cache do Maven; (3) compilação e execução dos testes
JUnit/MockMvc (`mvn clean verify`); (4) geração do `.jar` (`mvn package`); (5) publicação do jar como artefato do
GitHub Actions. O deploy na Railway é disparado automaticamente a cada push na branch configurada, o que completa o CD.

**e) Como o monitoramento contribui para melhorar a disponibilidade da aplicação?**
O endpoint `/status` permite que uma ferramenta (ou a própria Railway, via *healthcheck*) confirme a todo momento se
a aplicação responde. Quando ela para, o problema é detectado rápido e pode ser reiniciada ou corrigida antes de
os usuários perceberem. Logs e métricas ajudam a achar a causa e a prevenir a repetição.

**f) Quais problemas poderiam ocorrer se o deploy continuasse sendo manual?**
Esquecer etapas, publicar uma versão diferente da testada, configurações distintas entre servidores, demora para
entregar, dificuldade de voltar atrás e dependência de uma pessoa específica. Também aumenta a chance de erro humano
e de falhas em produção, sem registro claro do que foi publicado.

**g) Como a equipe poderia melhorar o processo DevOps em uma próxima versão?**
Adicionar banco de dados persistente (hoje os dados ficam em memória), validação dos dados de entrada, relatório de
cobertura de testes (JaCoCo), análise estática de qualidade, ambientes separados de homologação e produção, publicação
da imagem Docker em um registro, *healthcheck* e alertas, e proteção da branch `main` exigindo pipeline verde e revisão.

## 3. Roteiro de deploy na Railway
1. Em https://railway.com crie a conta e entre com o GitHub.
2. **New Project → Deploy from GitHub repo** → escolha `monitoramento-maquinas`.
3. A Railway detecta o `Dockerfile` e faz o build. A aplicação lê a porta da variável `PORT`
   (`server.port=${PORT:8080}`); não é preciso configurar nada manualmente.
4. Em **Settings → Networking → Generate Domain** gere a URL pública.
5. (Opcional) Em **Settings → Healthcheck Path** informe `/status`.
6. Teste no Thunder Client usando a URL pública (variável `baseUrl` da coleção) e tire os prints.

## 4. Roteiro da explicação oral do fluxo DevOps
1. Desenvolvi a API em Spring Boot e versionei no GitHub usando `main`, `develop` e branches `feature/*`.
2. A cada push, o GitHub Actions baixa o código, configura o Java 21, compila, roda os 15 testes MockMvc e gera o jar.
3. Se algum teste falhar, o pipeline fica vermelho e a mudança não é considerada pronta.
4. O Dockerfile empacota a aplicação, e a Railway constrói a imagem a partir do repositório e publica na nuvem.
5. Valido o ambiente publicado com o `/status` e com as requisições GET, POST, PUT e DELETE no Thunder Client.
6. Resultado: rastreabilidade, testes padronizados, entrega automática e menos erros manuais.

## 5. Comandos Git sugeridos (para ter histórico, branches e merges documentados)
```bash
# 0) remover do repositório a pasta m2repo (jars) que foi commitada por engano
git rm -r --cached m2repo
git commit -m "chore: remove m2repo do versionamento"

# 1) branch de desenvolvimento
git checkout -b develop && git push -u origin develop

# 2) uma branch por funcionalidade
git checkout -b feature/crud-maquinas
git add . && git commit -m "feat: adiciona PUT e DELETE em /maquinas"
git push -u origin feature/crud-maquinas
# abra um Pull Request feature/crud-maquinas -> develop no GitHub e faça o merge

git checkout develop && git pull
git checkout -b feature/testes
git add . && git commit -m "test: amplia testes MockMvc dos endpoints"
git push -u origin feature/testes
# PR feature/testes -> develop

# 3) quando estiver estável, PR develop -> main (o pipeline roda no PR e no merge)
```
