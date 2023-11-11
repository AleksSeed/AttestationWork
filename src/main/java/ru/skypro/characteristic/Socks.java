package ru.skypro.characteristic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Socks {
    private Color color;
    private CottonPart cottonPart;
    private Size size;
    private int quality;


    public void put(long l, Socks socks) {
    }
}
