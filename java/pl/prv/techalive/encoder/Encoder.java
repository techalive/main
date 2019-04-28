package pl.prv.techalive.encoder;

import pl.prv.techalive.encoder.builders.BuilderAES;
import pl.prv.techalive.encoder.builders.BuilderARCFOUR;
import pl.prv.techalive.encoder.builders.BuilderDES;
import pl.prv.techalive.encoder.hashes.Hash;
import pl.prv.techalive.encoder.builders.BuilderBlowfish;
import pl.prv.techalive.encoder.builders.BuilderDESede;
import pl.prv.techalive.encoder.builders.BuilderHMAC;
import pl.prv.techalive.encoder.builders.BuilderPBE;
import pl.prv.techalive.encoder.builders.BuilderRSA;

/**
 * Main Encoder class
 * From this class you can get access to all encryption methods
 */
public class Encoder {

    private Encoder() {

    }

    public static BuilderAES BuilderAES() {
        return new BuilderAES();
    }

    public static BuilderARCFOUR BuilderARCFOUR() {
        return new BuilderARCFOUR();
    }

    public static BuilderDES BuilderDES() {
        return new BuilderDES();
    }

    public static BuilderDESede BuilderDESede() {
        return new BuilderDESede();
    }

    public static BuilderRSA BuilderRSA() {
        return new BuilderRSA();
    }

    public static BuilderHMAC BuilderHMAC() {
        return new BuilderHMAC();
    }

    public static BuilderPBE BuilderPBE() {
        return new BuilderPBE();
    }

    public static BuilderBlowfish BuilderBlowfish() {
        return new BuilderBlowfish();
    }

    public static Hash Hashes() {
        return new Hash();
    }
}
