document.addEventListener('DOMContentLoaded', function() {
    const pessoaForm = document.getElementById('pessoaForm');
    const pessoasTable = document.getElementById('pessoasTable').getElementsByTagName('tbody')[0];
    const listarTodosBtn = document.getElementById('listarTodosBtn');
    const tableSection = document.getElementById('tableSection');
    const searchForm = document.getElementById('searchForm');
    const searchResult = document.getElementById('searchResult');
    const searchBtn = searchForm.querySelector('button[type="submit"]');
    let pessoasCache = []; // Cache para armazenar pessoas carregadas
    let tabelaVisivel = false; // Controle para a visibilidade da tabela

    pessoaForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const id = document.getElementById('id').value;
        const nome = document.getElementById('nome').value;
        const cpf = document.getElementById('cpf').value;
        const dataNascimento = document.getElementById('dataNascimento').value;
        const email = document.getElementById('email').value;

        const pessoa = {
            id,
            nome,
            cpf,
            dataNascimento,
            email
        };

        const mode = pessoaForm.dataset.mode; // Obtém o modo do formulário (criação ou edição)

        // Verificar duplicação
        if (isDuplicate(cpf, email, id)) {
            alert('Pessoa com este CPF ou email já existe.');
            return;
        }

        if (mode === 'edit' && id) {
            // Atualizar pessoa
            fetch(`http://localhost:8080/pessoas/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(pessoa)
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Falha ao atualizar pessoa: ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                console.log('Pessoa atualizada com sucesso:', data);
                resetForm();
                fetchPessoas();
            })
            .catch(error => console.error('Erro ao atualizar pessoa:', error));
        } else {
            // Criar pessoa
            fetch('http://localhost:8080/pessoas', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(pessoa)
            })
            .then(response => {
                if (response.status === 409) {
                    throw new Error('Pessoa com o mesmo CPF ou email já existe.');
                }
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Falha ao criar pessoa: ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                console.log('Pessoa criada com sucesso:', data);
                resetForm();
                fetchPessoas();
            })
            .catch(error => console.error('Erro ao criar pessoa:', error));
            
        }

        // Reseta o modo do formulário após a operação
        pessoaForm.dataset.mode = '';
    });

    function resetForm() {
        document.getElementById('id').value = '';
        document.getElementById('nome').value = '';
        document.getElementById('cpf').value = '';
        document.getElementById('dataNascimento').value = '';
        document.getElementById('email').value = '';
        pessoaForm.dataset.mode = ''; // Limpa o modo ao resetar o formulário
    }

    listarTodosBtn.addEventListener('click', function() {
        if (tabelaVisivel) {
            // Se a tabela está visível, esconda-a
            tableSection.style.display = 'none';
            listarTodosBtn.textContent = 'Listar Todos'; // Muda o texto do botão
        } else {
            // Se a tabela não está visível, mostre-a e carregue os dados
            tableSection.style.display = 'block';
            listarTodosBtn.textContent = 'Ocultar Lista'; // Muda o texto do botão
            fetchPessoas(); // Carrega os dados da tabela
        }
        tabelaVisivel = !tabelaVisivel; // Alterna a visibilidade da tabela
    });

    function fetchPessoas() {
        fetch('http://localhost:8080/pessoas')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Falha ao buscar pessoas');
                }
                return response.json();
            })
            .then(pessoas => {
                pessoasCache = pessoas; // Atualizar o cache
                renderPessoas(pessoas); // Atualiza a tabela
            })
            .catch(error => console.error('Erro:', error));
    }

    function renderPessoas(pessoas) {
        pessoasTable.innerHTML = ''; // Limpa o conteúdo da tabela
        pessoas.forEach(pessoa => {
            const row = pessoasTable.insertRow();
            row.innerHTML = `
                <td>${pessoa.id}</td>
                <td>${pessoa.nome}</td>
                <td>${pessoa.cpf}</td>
                <td>${pessoa.dataNascimento}</td>
                <td>${pessoa.email}</td>
                <td>
                    <button onclick="editPessoa(${pessoa.id})">Editar</button>
                    <button onclick="deletePessoa(${pessoa.id})">Excluir</button>
                </td>
            `;
        });
    }

    function isDuplicate(cpf, email, currentId) {
        return pessoasCache.some(pessoa => 
            (pessoa.cpf === cpf || pessoa.email === email) && pessoa.id !== currentId
        );
    }

    searchForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const searchId = document.getElementById('searchId').value;

        if (!searchId) {
            alert('Por favor, insira um ID para buscar.');
            return;
        }

        fetch(`http://localhost:8080/pessoas/${searchId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Pessoa não encontrada');
                }
                return response.json();
            })
            .then(pessoa => {
                searchResult.innerHTML = `
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>CPF</th>
                                <th>Data de Nascimento</th>
                                <th>Email</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>${pessoa.id}</td>
                                <td>${pessoa.nome}</td>
                                <td>${pessoa.cpf}</td>
                                <td>${pessoa.dataNascimento}</td>
                                <td>${pessoa.email}</td>
                            </tr>
                        </tbody>
                    </table>
                `;
                searchBtn.textContent = 'Ocultar Busca'; // Muda o texto do botão para 'Ocultar Busca'

                // Limpar o campo de busca após encontrar o ID
                document.getElementById('searchId').value = '';
            })
            .catch(error => {
                searchResult.innerHTML = `<p>${error.message}</p>`;
                console.error('Erro:', error);
            });
    });

    searchBtn.addEventListener('click', function() {
        if (searchBtn.textContent === 'Ocultar Busca') {
            searchResult.innerHTML = '';
            searchBtn.textContent = 'Buscar';
        }
    });

    window.editPessoa = function(id) {
        fetch(`http://localhost:8080/pessoas/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Falha ao buscar pessoa');
                }
                return response.json();
            })
            .then(pessoa => {
                document.getElementById('id').value = pessoa.id;
                document.getElementById('nome').value = pessoa.nome;
                document.getElementById('cpf').value = pessoa.cpf;
                document.getElementById('dataNascimento').value = pessoa.dataNascimento;
                document.getElementById('email').value = pessoa.email;

                // Configura o modo do formulário como edição
                pessoaForm.dataset.mode = 'edit';
            })
            .catch(error => console.error('Erro:', error));
    };

    window.deletePessoa = function(id) {
        fetch(`http://localhost:8080/pessoas/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Falha ao excluir pessoa');
            }
            fetchPessoas(); // Atualiza a lista de pessoas após exclusão
        })
        .catch(error => console.error('Erro:', error));
    };
});
