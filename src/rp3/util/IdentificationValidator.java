package rp3.util;

import rp3.data.Constants;
import rp3.data.models.GeneralValue;
import rp3.db.sqlite.DataBase;

public class IdentificationValidator {

    private static final int NUM_PROVINCIAS = 24;
    // public static String rucPrueba = “1790011674001″;
    private static int[] coeficientes = { 4, 3, 2, 7, 6, 5, 4, 3, 2 };
    private static int constante = 11;
    public static boolean ValidateIdentification(DataBase db, String number, int type)
    {
        GeneralValue validator = GeneralValue.getGeneralValue(db, Constants.GENERAL_TABLE_IDENTIFICATION, type + "");
        if(validator == null)
            return true;
        else {
            if(number.length() == Integer.parseInt(validator.getValue())) {
                if(validator.getReference1().equalsIgnoreCase("1")) {
                    switch (type) {
                        case 1:
                            return ValidateRuc(number);
                        case 2:
                            return ValidateCedula(number);
                        case 3:
                            return ValidateDefault(number);
                        default:
                            return ValidateDefault(number);
                    }
                }
                else {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }

    private static boolean ValidateRuc(String number) {

        if (ValidateRucSociedades(number))
            return true;
        else if (ValidateRucNaturales(number))
            return true;
        else if (ValidateRucEstatal(number))
            return true;
        else
            return false;


    }
    private static boolean ValidateCedula(String number) {
        boolean cedulaCorrecta = false;

        try {


            int tercerDigito = Integer.parseInt(number.substring(2, 3));
            if (tercerDigito < 6) {
                // Coeficientes de validación cédula
                // El decimo digito se lo considera dígito verificador
                int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                int verificador = Integer.parseInt(number.substring(9, 10));
                int suma = 0;
                int digito = 0;
                for (int i = 0; i < (number.length() - 1); i++) {
                    digito = Integer.parseInt(number.substring(i, i + 1)) * coefValCedula[i];
                    suma += ((digito % 10) + (digito / 10));
                }

                if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                    cedulaCorrecta = true;
                } else if ((10 - (suma % 10)) == verificador) {
                    cedulaCorrecta = true;
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
            cedulaCorrecta = true;

        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
        }
        return cedulaCorrecta;
    }
    private static boolean ValidateDefault(String number)
    {
        return number.length() == 10;
    }

    private static boolean ValidateRucSociedades(String number)
    {
        boolean resp_dato = false;
        int prov = Integer.parseInt(number.substring(0, 2));
        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
            resp_dato = false;
        }

        int[] d = new int[10];
        int suma = 0;

        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(number.charAt(i) + "");
        }

        for (int i = 0; i < d.length - 1; i++) {
            d[i] = d[i] * coeficientes[i];
            suma += d[i];
        }

        int aux, resp;

        aux = suma % constante;
        resp = constante - aux;

        resp = (aux == 0) ? 0 : resp;

        if (resp == d[9]) {
            resp_dato = true;
        } else {
            resp_dato = false;
        }

        return resp_dato;
    }
    private static boolean ValidateRucNaturales(String number)
    {
        return ValidateCedula(number.substring(0,10));
    }
    private static boolean ValidateRucEstatal(String number)
    {
        final int prov = Integer.parseInt(number.substring(0, 2));
        boolean resp = false;

        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
        resp = false;
    }

        // boolean val = false;
        Integer v1, v2, v3, v4, v5, v6, v7, v8, v9;
        Integer sumatoria;
        Integer modulo;
        Integer digito;
        int[] d = new int[number.length()];

        for (int i = 0; i < d.length; i++) {
        d[i] = Integer.parseInt(number.charAt(i) + "");
    }

        v1 = d[0] * 3;
        v2 = d[1] * 2;
        v3 = d[2] * 7;
        v4 = d[3] * 6;
        v5 = d[4] * 5;
        v6 = d[5] * 4;
        v7 = d[6] * 3;
        v8 = d[7] * 2;
        v9 = d[8];

        sumatoria = v1 + v2 + v3 + v4 + v5 + v6 + v7 + v8;
        modulo = sumatoria % 11;
        if (modulo == 0) {
            return true;
        }
        digito = 11 - modulo;

        if (digito.equals(v9)) {
            resp = true;
        } else {
            resp = false;
        }
        return resp;
    }
}
