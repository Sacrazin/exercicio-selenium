# Testes End-to-End com Selenium WebDriver

## Identificação
* **Disciplina:** Qualidade e Teste de Software
* **Aplicação Testada:** [Automation Exercise](https://automationexercise.com/)

---

## Modelação de Testes: Classes de Equivalência e Valores Limite

Para o cenário negativo de autenticação (**Test Case 3**), aplicaram-se as técnicas de teste funcional de caixa preta (PCE e AVL) sobre as entradas do formulário:

| ID | Técnica Aplicada | E-mail de Entrada | Palavra-passe de Entrada | Resultado Esperado |
|---|---|---|---|---|
| **CT01** | PCE (E-mail não registado) | `usuario_inexistente@dominio.com` | `Senha123` | Mensagem: *"Your email or password is incorrect!"* |
| **CT02** | PCE (Palavra-passe errada) | `cadastrado@teste.com` | `SenhaErrada` | Mensagem: *"Your email or password is incorrect!"* |
| **CT03** | PCE (Domínio alternativo) | `outro_usuario@empresa.com.br` | `12345678` | Mensagem: *"Your email or password is incorrect!"* |
| **CT04** | AVL (Limite mín.: 1 caráter) | `usuario_valido@dominio.com` | `a` | Mensagem: *"Your email or password is incorrect!"* |
| **CT05** | AVL (Palavra-passe longa) | `usuario_generico@teste.com` | `SenhaMuitoLongaComMaisDe30Caracteres` | Mensagem: *"Your email or password is incorrect!"* |

---

## Cenários Automatizados
1. **Test Case 1 (Register User):** Registo de nova conta com preenchimento completo de formulário, validação de login e subsequente eliminação da conta (*Delete Account*).
2. **Test Case 3 (Login User with incorrect email and password):** Validação da mensagem de erro com credenciais incorretas, incluindo bateria de testes parametrizados com 5 entradas.