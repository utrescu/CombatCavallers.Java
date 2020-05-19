package net.xaviersala.combatcavallers.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.xaviersala.combatcavallers.LlocOnPicar;
import net.xaviersala.combatcavallers.lluitador.ILluitador;

/**
 * Lloc en el que es desenvolupa la lluita
 *
 * Per defecte tots els lluitadors tenen 20 de vida
 */
public class Ring implements IRing {

    private static final int PALLISSA = 5;
    private static final int VIDAINICIAL = 20;

    Random aleatori = new Random();
    private final static Logger LOGGER = Logger.getLogger("bitacora.subnivel.Control");

    List<ICombatents> _Lluitadors;

    public Ring() {
        _Lluitadors = new ArrayList<ICombatents>(2);
    }

    /**
     * Col·loca dos lluitadors al ring
     *
     * @param name lluitador1 Primer lluitador
     * @param name lluitador2 Segon lluitador
     */
    public void EntradaLluitadors(ILluitador lluitador1, ILluitador lluitador2) {

        _Lluitadors.clear();
        _Lluitadors.add(new Resultat(lluitador1, VIDAINICIAL));
        _Lluitadors.add(new Resultat(lluitador2, VIDAINICIAL));
    }

    /**
     * Fa que els dos lluitadors lluitin fins que s'acabi el combat
     *
     * @return Una llista IResultat amb el resultat del combat
     */
    public List<IResultat> Lluiteu() {
        if (_Lluitadors.size() != 2) {
            LOGGER.log(Level.SEVERE, "No es pot començar el combat. Falten Lluitadors");
            return null;
        }

        int elQuePica = aleatori.nextInt(2);
        LOGGER.log(Level.INFO, "Sorteig de qui comença: ....  " + _Lluitadors.get(elQuePica).getNom());

        while (!_Lluitadors.get(0).EsKo() && !_Lluitadors.get(1).EsKo()) {
            int elQueRep = (elQuePica + 1) % 2;
            List<LlocOnPicar> proteccio = _Lluitadors.get(elQueRep).getLluitador().Protegeix();
            LlocOnPicar pica = _Lluitadors.get(elQuePica).getLluitador().Pica();

            boolean haRebut = proteccio.contains(pica);
            if (haRebut) {
                _Lluitadors.get(elQueRep).TreuVida();
                LOGGER.log(Level.INFO, _Lluitadors.get(elQueRep).getNom() + " rep un cop al " + pica + " de "
                        + _Lluitadors.get(elQuePica).getNom());

            } else {
                LOGGER.log(Level.INFO, _Lluitadors.get(elQueRep).getNom() + " atura el cop al " + pica + " de "
                        + _Lluitadors.get(elQuePica).getNom());
            }

            LOGGER.log(Level.WARNING, _Lluitadors.get(0) + " vs " + _Lluitadors.get(1));
            elQuePica = elQueRep;
        }

        int guanya = _Lluitadors.get(0).EsKo() ? 1 : 0;
        int perd = (guanya + 1) % 2;

        ILluitador guanyador = _Lluitadors.get(guanya).getLluitador();
        ILluitador perdedor = _Lluitadors.get(perd).getLluitador();

        String comentariLocutor = (_Lluitadors.get(guanya).getVida() - _Lluitadors.get(perd).getVida()) > PALLISSA
                ? "Quina Pallissa!!"
                : "";

        LOGGER.log(Level.INFO, perdedor.getNom() + " cau a terra!");
        LOGGER.log(Level.INFO, "VICTÒRIA DE " + guanyador.getNom() + "!!! " + comentariLocutor);

        return toIResultat();

    }

    private List<IResultat> toIResultat() {
        List<IResultat> resultat = new ArrayList<IResultat>();
        resultat.add(_Lluitadors.get(0));
        resultat.add(_Lluitadors.get(1));
        return resultat;
    }
}
