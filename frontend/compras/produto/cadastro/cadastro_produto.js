document.addEventListener('DOMContentLoaded', function() {
  const form = document.querySelector('form');
  const h2 = document.querySelector('h2');

  form.addEventListener('submit', async function(event) {
    let msgElemento = document.getElementById('msgErroDiv');
    if (msgElemento) {
      msgElemento.remove();
    }

    event.preventDefault();

    const carregandoMsg = document.createElement('div');
    carregandoMsg.textContent = 'Executando...';
    carregandoMsg.style.color = 'blue';
    carregandoMsg.style.fontWeight = 'bold';
    h2.parentNode.insertBefore(carregandoMsg, h2.nextSibling);

    const overlay = document.createElement('div');
    overlay.style.position = 'fixed';
    overlay.style.top = '0';
    overlay.style.left = '0';
    overlay.style.width = '100%';
    overlay.style.height = '100%';
    overlay.style.backgroundColor = 'rgba(0,0,0,0.1)';
    overlay.style.zIndex = '1000';
    document.body.appendChild(overlay);

    try {
      const formData = {
        nomeProduto: document.getElementById('nomeProduto').value,
        precoProduto: document.getElementById('precoProduto').value
      };

      let url = urlBase() + "/api/v1/produto/cadastro";
      const resposta = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      });

      if (resposta.status === 201) {
        alert('Produto cadastrado com sucesso!');
        location.href = "cadastro_produto.html";
      } else {
        const dadosErro = await resposta.json();
        let erro = document.querySelector('.message');
        if (!erro) {
          erro = document.createElement('div');
          erro.className = 'message';
          form.parentNode.insertBefore(erro, form);
        }
        erro.textContent = dadosErro.message || 'Tivemos um erro com o cadastro do seu produto. Tente novamente.';
        erro.style.color = 'red';
      }
    } catch (error) {
      console.error('Erro:', error);
      let msgElemento = document.querySelector('.message');
      if (!msgElemento) {
        msgElemento = document.createElement('div');
        msgElemento.className = 'msg';
        msgElemento.id = 'msgErroDiv';
        form.parentNode.insertBefore(msgElemento, form);
      }
      msgElemento.textContent = 'O servidor não está disponível';
      msgElemento.style.color = 'red';
    } finally {
      if (carregandoMsg.parentNode) {
        carregandoMsg.parentNode.removeChild(carregandoMsg);
      }
      if (overlay.parentNode) {
        overlay.parentNode.removeChild(overlay);
      }
    }
  });
});