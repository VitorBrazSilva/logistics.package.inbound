import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 100, // Número de usuários simultâneos
  duration: '30s', // Duração total do teste
};

const BASE_URL = 'http://localhost:8080'; // Atualize para a URL real da sua API

export default function () {
  let packagePayload = JSON.stringify({
    description: 'Teste de carga',
    sender: 'Loja Teste',
    recipient: 'Cliente Teste',
    estimatedDeliveryDate: '2025-12-25'
  });
  
  let params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // Criar um pacote
  let createRes = http.post(`${BASE_URL}/api/shippingpackage`, packagePayload, params);
  check(createRes, {
    'Criado com sucesso': (res) => res.status === 201,
  });

  let packageId = createRes.json().id;

  // Atualizar status do pacote
//   if (packageId) {
//     let statusPayload = JSON.stringify({ status: 'IN_TRANSIT' });
//     let updateRes = http.patch(`${BASE_URL}/api/shippingpackage/${packageId}/status`, statusPayload, params);
//     check(updateRes, {
//       'Status atualizado': (res) => res.status === 200,
//     });
//   }

  // Buscar pacotes
//   let getRes = http.get(`${BASE_URL}/api/shippingpackage`);
//   check(getRes, {
//     'Consulta bem-sucedida': (res) => res.status === 200,
//   });

  sleep(1); // Pausa entre requisições para simular usuários reais
}