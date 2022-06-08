package com.company;

public enum PlayerType {
    Ponter,
    Banker;

    public String toString() {
        String str = null;
        switch (this) {
            case Ponter -> str = "Ponter";
            case Banker -> str = "Banker";
        }
        return str;
    }
}
