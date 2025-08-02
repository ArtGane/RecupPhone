import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SalecPhone {

    public static void main(String[] args) {
        ArrayList<String> ipList = getIpAddresses();

        // Parcourir chaque adresse IP pour récupérer le nom de l'appareil et le type
        for (String ipAddress : ipList) {
            String hostname = getHostName(ipAddress);
            String deviceType = getDeviceType(hostname);

            if (hostname != null) {
                System.out.println("Nom de l'appareil pour " + ipAddress + " : " + hostname + " | Type : " + deviceType);
            } else {
                System.out.println("Nom inconnu pour l'adresse IP : " + ipAddress + " | Type : " + deviceType);
            }
        }
    }

    private static String getDeviceType(String hostname) {
        if (hostname == null) return "Inconnu";
        hostname = hostname.toLowerCase();
        if (hostname.contains("iphone") || hostname.contains("ios")) return "iPhone/iOS";
        if (hostname.contains("android")) return "Android";
        if (hostname.contains("pc") || hostname.contains("desktop") || hostname.contains("windows")) return "PC";
        if (hostname.contains("mac") || hostname.contains("apple")) return "Mac";
        if (hostname.contains("printer")) return "Imprimante";
        if (hostname.contains("router")) return "Routeur";
        return "Inconnu";
    }

private static ArrayList<String> getIpAddresses() {
    ArrayList<String> ipList = new ArrayList<>();
    try {
        Process process = Runtime.getRuntime().exec("nmap -sn 192.168.1.0");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            String ipRegex = "\\b(\\d{1,3}\\.){3}\\d{1,3}\\b";
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(ipRegex).matcher(line);
            if (matcher.find()) {
                String ip = matcher.group();
                ipList.add(ip);
            }
        }
        reader.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return ipList;
}

    private static String getHostName(String ip) {
        try {
            Process process = Runtime.getRuntime().exec("nslookup " + ip);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name:")) {
                    return line.split(":")[1].trim();
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}