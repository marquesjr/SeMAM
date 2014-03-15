Serviço Móvel de Atendimento Médico
=========

O cenário baseia-se no atendimento à chamadas de emergência, no qual um sistema nomeado Serviço Móvel de Atendimento Médico – SeMAM, oferece um serviço móvel de atendimento médico, que recebe chamadas de emergência e envia uma ambulância ao local da chamada. O serviço também envia solicitações de ajuda a voluntários que estiverem próximos ao local da emergência. As seguintes situações podem ser identificadas neste cenário:

  - *HelpRequest*: existe quando um pedido de ajuda foi enviado a um voluntário que estiver a no máximo 200 metros do paciente com uma emergência médica, e a ambulância ainda não chegou ao local;

- *OngoingEmergencyCare*: existe enquanto o paciente estiver sob cuidados de paramédicos. Portanto, ela passa a existir no momento em que a ambulância chega ao local da emergência e se mantém ativa até que o paciente chegue ao hospital ou venha a falecer;

- *Bradycardia*: existe quando uma situação de OngoingEmergencyCare já existe para o paciente e sua frequência cardíaca for menor ou igual a 60 batidas por minuto;

- *Tachycardia*: existe quando uma situação de OngoingEmergencyCare já existe para o paciente e sua frequência cardíaca supera 100 batidas por minuto;

- *CardiacArrest*: existe quando uma situação de OngoingEmergencyCare já existe para o paciente e sua frequência cardíaca é nula;

- *HighUrgency*: existe quando o paciente sofre uma parada cardíaca por mais de 3 minutos, ou seja, quando existir uma situação CardiacArrest para aquele paciente, por mais de 3 minutos.

Implementação
----
A implementação foi dividida em duas partes:

A primeira, denominada *SeMAM Server*, implementa o mapa do cenário, registra chamadas de emergência e simula as posições dos objetos no cenário. 

A segunda parte, denominada *SeMAM Ambulance*, é responsavel por receber e responder à chamadas de emergência recebidas de SeMAM Server.

SeMAM É um ambiente web com front-end desenvolvido em [AngularJS][1], back-end em [Play Framework][2] e situações implementadas em [SCENE][3].


Versão
----

1.0

Instalação
--------------

- Instalar [JDK 7][4] ou superior;


Clonar repositório:

```sh
git clone https://github.com/marquesjr/SeMAM.git
```


Licença
----

GNU GPL


[1]:http://angularjs.org/
[2]:http://www.playframework.com/
[3]:http://git.pop-es.rnp.br/scene
[4]:http://www.oracle.com/

    