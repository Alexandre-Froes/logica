function inicializarAlgoritmo() {
    let populacao = new Array();
    let notas = new Array();

    const medicos = gerarMedicos(7);

    function gerarMedicos(qtdCG) {
        const TOTAL_MEDICOS = 25;

        if (qtdCG > TOTAL_MEDICOS) {
            throw new Error("Quantidade de CG maior que o total de médicos");
        }

        const especialidadesOrdem = ["PE", "GI", "OR", "CA"];

        const medicos = [];
        let id = 0;

        for (let i = 0; i < qtdCG; i++) {
            medicos.push({ id: id++, especialidade: "CG" });
        }

        let restante = TOTAL_MEDICOS - qtdCG;
        let idxEsp = 0;

        while (restante > 0) {
            medicos.push({
                id: id++,
                especialidade: especialidadesOrdem[idxEsp]
            });

            restante--;
            idxEsp = (idxEsp + 1) % especialidadesOrdem.length;
        }

        return medicos;
    }

    const getRandomInt = (max) => Math.floor(Math.random() * max);

    console.log(populacao);

    function geraPopulacao(popInicial) {
        for (var i = 0; i < popInicial; ++i) {
            let cromossomo = [];
            for (var j = 0; j < 189; ++j) {
                let randomId = getRandomInt(25);
                cromossomo.push(randomId);
            }
            populacao.push(cromossomo);
        }
    }

    function avaliacao(cromossomo, config) {
        let penalidades = {
            repeticaoTurno: 0,
            faltaClinico: 0,
            medicosRepetidosUnidade: 0,
            conflitoTurno: 0,
            cargaHoraria: 0,
            muitoClinico: 0,
            total: 0
        };

        const turnosPorMedico = new Uint8Array(25);
        let idsTurnoAnterior = new Set();

        for (let idDia = 0; idDia < cromossomo.length; idDia += 27) {
            
            for (let j = 0; j < 27; j += 9) {
                const vistosNoTurno = new Set();

                for (let k = 0; k < 9; k += 3) {
                    let contagemCG = 0;
                    const medicosNaUnidade = new Set();

                    for (let m = 0; m < 3; m++) {
                        const idMed = cromossomo[idDia + j + k + m];

                        turnosPorMedico[idMed]++;

                        if (medicos[idMed].especialidade === 'CG') {
                            contagemCG++;
                        }

                        regraRepeticaoTurno(idMed, idsTurnoAnterior, penalidades, config);

                        medicosNaUnidade.add(idMed);
                        vistosNoTurno.add(idMed);
                    }

                    regraClinicoGeralUnidade(contagemCG, penalidades, config);
                    regraMedicosDistintosUnidade(medicosNaUnidade.size, penalidades, config);
                }

                regraConflitoInternoTurno(vistosNoTurno.size, penalidades, config);

                idsTurnoAnterior = vistosNoTurno;
            }
        }

        regraCargaHorariaSemanal(turnosPorMedico, penalidades, config);

        penalidades.total =
            penalidades.repeticaoTurno +
            penalidades.faltaClinico +
            penalidades.medicosRepetidosUnidade +
            penalidades.conflitoTurno +
            penalidades.cargaHoraria +
            penalidades.muitoClinico;

        return penalidades;
    }

    function regraRepeticaoTurno(idMed, idsTurnoAnterior, penalidades, config) {
        if (idsTurnoAnterior.has(idMed)) {
            penalidades.repeticaoTurno += config.heavyPen * 3;
        }
    }   

    function regraClinicoGeralUnidade(contagemCG, penalidades, config) {
        if (contagemCG === 0) {
            penalidades.faltaClinico += config.heavyPen;
        }

        if (contagemCG > 1) {
            penalidades.muitoClinico += (contagemCG - 1) * config.mediumPen;
        }
    }

    function regraMedicosDistintosUnidade(total, penalidades, config) {
        if (total < 3) {
            penalidades.medicosRepetidosUnidade += 
                (3 - total) * config.heavyPen;
        }
    }

    function regraConflitoInternoTurno(total, penalidades, config) {
        let maxDistintos = 9;

        if (total < maxDistintos) {
            penalidades.conflitoTurno += 
                (maxDistintos - total) * config.heavyPen;
        }
    }

    function regraCargaHorariaSemanal(turnosPorMedico, penalidades, config) {
        let turnosMax = 5; // 8 horas por dia
        // let turnosMax = 6.66; // 6 horas por dia

        for (let i = 0; i < turnosPorMedico.length; i++) {
            if (turnosPorMedico[i] > turnosMax) {
                penalidades.cargaHoraria += 
                    Math.round((turnosPorMedico[i] - turnosMax) * config.softPen, 0);
            }
        }
    }

    function avaliaCromossomo() {
        const CONFIGPEN = {
            softPen: 1,
            mediumPen: 2,
            hardPen: 4,
            heavyPen: 10
        }

        let populacaoComNotas = new Array();

        for (const cromossomo of populacao) {
            const resultado = avaliacao(cromossomo, CONFIGPEN);

            populacaoComNotas.push({
                cromossomo,
                nota: resultado.total,
                detalhes: resultado
            });
        }

        return populacaoComNotas.sort((a, b) => a.nota - b.nota);
    }

    function selecaoTorneio(populacaoComNotas, tamanhoTorneio = 10) {
        let melhorIdx = 0;
        
        for (let i = 1; i < tamanhoTorneio; i++) {
            const randomIdx = getRandomInt(populacaoComNotas.length);
            if (populacaoComNotas[randomIdx].nota < populacaoComNotas[melhorIdx].nota) {
                melhorIdx = randomIdx;
            }
        }
        
        return populacaoComNotas[melhorIdx].cromossomo;
    }

    function crossover(pai1, pai2, crossoverPoint = 9) {
        const filho1 = [];
        const filho2 = [];

        for (let i = 0; i < pai1.length; i += crossoverPoint) {
            if (Math.random() < 0.5) {
                filho1.push(...pai1.slice(i, i + crossoverPoint));
                filho2.push(...pai2.slice(i, i + crossoverPoint));
            } else {
                filho1.push(...pai2.slice(i, i + crossoverPoint));
                filho2.push(...pai1.slice(i, i + crossoverPoint));
            }
        }

        return [filho1, filho2];
    }

    function mutacao(cromossomo, probMutacao) {
        const cromossomoMutado = [...cromossomo];
        
        for (let i = 0; i < cromossomoMutado.length; i++) {
            if (Math.random() < probMutacao) {
                cromossomoMutado[i] = getRandomInt(25);
            }
        }
        
        return cromossomoMutado;
    }

    function imprimirMelhorIndividuo(melhorSolucao, medicos) {
        const cromossomo = melhorSolucao.cromossomo;

        console.log(`\n===== MELHOR INDIVÍDUO =====`);
        console.log(`Geração: ${melhorSolucao.geracao + 1}`);
        console.log(`Fitness: ${melhorSolucao.nota}`);
        console.log("Detalhes das penalidades:");
        console.table(melhorSolucao.detalhes);

        const dias = 7;
        const unidades = 3;
        const turnos = 3;
        const medicosPorTurno = 3;

        let idx = 0;

        for (let dia = 0; dia < dias; dia++) {
            console.log(`\n--- Dia ${dia + 1} ---`);
            for (let unidade = 0; unidade < unidades; unidade++) {
                console.log(`  Unidade ${unidade + 1}:`);
                for (let turno = 0; turno < turnos; turno++) {
                    const idsTurno = cromossomo.slice(idx, idx + medicosPorTurno);
                    const nomesTurno = idsTurno.map(id => `${medicos[id].especialidade}-${medicos[id].id}`);
                    console.log(`    Turno ${turno + 1}: ${nomesTurno.join(", ")}`);
                    idx += medicosPorTurno;
                }
            }
        }
    }

    function executarAlgoritmoGenetico() {
        const popInicial = 300;
        const probMutacao = 0.04;
        const probCrossover = 0.7;
        const numGeracoes = 2000;
        const comElitismo = true;
        const porcentagemElite = 0.02;

        geraPopulacao(popInicial);

        let melhorSolucao = null;

        for (let geracao = 0; geracao < numGeracoes; geracao++) {
            populacao = populacao.slice(0, popInicial);
            const populacaoComNotas = avaliaCromossomo();

            const melhorDaGeracao = populacaoComNotas[0];

            
            if (!melhorSolucao || melhorDaGeracao.nota < melhorSolucao.nota) {
                melhorSolucao = { ...melhorDaGeracao, geracao };
            }
            
            if (geracao === 0 ) {
                console.log("Primeira geração:");
                console.table(melhorSolucao.detalhes);
            }

            let novaPopulacao = [];

            if (comElitismo) {
                const melhores = populacaoComNotas.slice
                    (0, Math.floor(porcentagemElite * popInicial));
                novaPopulacao = [...melhores.map(p => p.cromossomo)];
            }

            while (novaPopulacao.length < popInicial) {
                const pai1 = selecaoTorneio(populacaoComNotas);
                const pai2 = selecaoTorneio(populacaoComNotas);

                let filho1 = pai1;
                let filho2 = pai2;

                if (Math.random() < probCrossover) {
                    [filho1, filho2] = crossover(pai1, pai2);
                }

                filho1 = mutacao(filho1, probMutacao);
                filho2 = mutacao(filho2, probMutacao);

                novaPopulacao.push(filho1);
                if (novaPopulacao.length < popInicial) {
                    novaPopulacao.push(filho2);
                }
            }

            populacao = novaPopulacao.slice(0, popInicial);
        }

            imprimirMelhorIndividuo(melhorSolucao, medicos);
    }

    populacao = [];
    const solucaoFinal = executarAlgoritmoGenetico();

    return solucaoFinal;
}

console.log("SISTEMA DE ESCALONAMENTO DE MEDICOS COM ALGORITMO GENETICO");
const resultado = inicializarAlgoritmo();