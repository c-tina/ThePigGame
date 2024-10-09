package com.example.practica2b;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.practica2b.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private int currentScore = 0; // Puntuación acumulada en el turno actual
    private int totalPlayer1 = 0; // Puntuación total del jugador 1
    private int totalPlayer2 = 0; // Puntuación total del jugador 2
    private String playerTurn = "Player1"; // Control de turno
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toast.makeText(this, "JUGADOR 1 EMPIEZA", Toast.LENGTH_SHORT).show();
    }

    public void lanzarDado(View view) {
        int numRand = (int) (Math.random() * 6) + 1;
        // Restablecer los valores antes de cada nuevo lanzamiento
        binding.lanzar.setText("LANZAR");
        binding.lanzar.setBackgroundColor(getResources().getColor(R.color.azul_600));
        binding.numDadoPlayer1.setBackgroundResource(R.color.transparente);
        binding.numDadoPlayer2.setBackgroundResource(R.color.transparente);
        binding.numDadoPlayer1.setText("0");
        binding.numDadoPlayer2.setText("0");

        // Verificar de quién es el turno
        if (playerTurn.equals("Player1")) {
            // Mostrar el número para Player1
            binding.numDadoPlayer1.setText(String.valueOf(numRand));
            actualizarImagenDado(numRand);

            // Si da 1, se reinicia la puntuación y se cambia el turno
            if (numRand == 1) {
                currentScore = 0;
                playerTurn = "Player2";
                binding.totalPlayer1.setText(String.valueOf(totalPlayer1));
                binding.lanzar.setText("FALLO");
                binding.lanzar.setBackgroundColor(getResources().getColor(R.color.rojo));
                binding.numDadoPlayer1.setBackgroundColor(getResources().getColor(R.color.rojo));
                binding.viewUp.setBackgroundColor(getResources().getColor(R.color.azul_100));
                binding.viewDown.setBackgroundColor(getResources().getColor(R.color.azul_500));

            } else {
                currentScore += numRand; // Acumular la puntuación
            }
            // Actualizar la puntuación total
            binding.totalPlayer1.setText(String.valueOf(totalPlayer1 + currentScore));

        } else if (playerTurn.equals("Player2")) {
            // Mostrar el número para Player2
            binding.numDadoPlayer2.setText(String.valueOf(numRand));
            actualizarImagenDado(numRand);

            // Si da 1, se reinicia la puntuación y se cambia el turno
            if (numRand == 1) {
                currentScore = 0;
                playerTurn = "Player1";
                binding.totalPlayer2.setText(String.valueOf(totalPlayer2));
                binding.lanzar.setText("FALLO");
                binding.lanzar.setBackgroundColor(getResources().getColor(R.color.rojo));
                binding.numDadoPlayer2.setBackgroundColor(getResources().getColor(R.color.rojo));
                binding.viewUp.setBackgroundColor(getResources().getColor(R.color.azul_500));
                binding.viewDown.setBackgroundColor(getResources().getColor(R.color.azul_100));

            } else {
                currentScore += numRand; // Acumular la puntuación
            }
            // Actualizar la puntuación total
            binding.totalPlayer2.setText(String.valueOf(totalPlayer2 + currentScore));
        }
    }

    // Cambia la imagen del dado según el número que se genere
    private void actualizarImagenDado(int numero) {
        switch (numero) {
            case 1:
                binding.imgDado.setImageResource(R.drawable.dado_rojo);
                break;
            case 2:
                binding.imgDado.setImageResource(R.drawable.dado_2);
                break;
            case 3:
                binding.imgDado.setImageResource(R.drawable.dado_3);
                break;
            case 4:
                binding.imgDado.setImageResource(R.drawable.dado_4);
                break;
            case 5:
                binding.imgDado.setImageResource(R.drawable.dado_5);
                break;
            case 6:
                binding.imgDado.setImageResource(R.drawable.dado_6);
                break;
            default:
                binding.imgDado.setImageResource(R.drawable.dado_1);
        }
    }

    public void pasarTurno(View view) {
        // Guardar la puntuación acumulada en el jugador correspondiente y cambiar al siguiente jugador
        if (playerTurn.equals("Player1")) {
            totalPlayer1 += currentScore;
            binding.totalPlayer1.setText(String.valueOf(totalPlayer1)); // Mostrar la puntuación total del jugador 1
            binding.numDadoPlayer1.setText(String.valueOf("0")); // Actualizar número de dado a 0
            playerTurn = "Player2";
            currentScore = 0; // Restablece la puntuación acumulada para el siguiente turno
        } else if (playerTurn.equals("Player2")){
            totalPlayer2 += currentScore;
            binding.totalPlayer2.setText(String.valueOf(totalPlayer2)); // Mostrar la puntuación total del jugador 2
            binding.numDadoPlayer2.setText(String.valueOf("0")); // Actualizar número de dado a 0
            playerTurn = "Player1";
            currentScore = 0; // Restablece la puntuación acumulada para el siguiente turno
        }

        // Verificar si hay un ganador
        if (verificarGanador()) {
            return;
        }

        // Actualizar visualmente de quién es el turno
        if (playerTurn.equals("Player1")) {
            binding.viewUp.setBackgroundResource(R.color.azul_500);
            binding.viewDown.setBackgroundResource(R.color.azul_100);
        } else if(playerTurn.equals("Player2")) {
            binding.viewDown.setBackgroundResource(R.color.azul_500);
            binding.viewUp.setBackgroundResource(R.color.azul_100);
        }
    }

    // Verifica si algún jugador ha llegado a 50 o más puntos y gana
    private boolean verificarGanador(){
        if (totalPlayer1 >= 50) {
            mostrarGanador("Jugador 1");
            return true;
        } else if (totalPlayer2 >= 50) {
            mostrarGanador("Jugador 2");
            return true;
        }
        return false;
    }

    // Muestra el ganador y oculta elementos de la interfaz
    private void mostrarGanador(String jugador) {
        binding.imgDado.setVisibility(View.GONE);
        binding.totalPlayer1.setVisibility(View.GONE);
        binding.totalPlayer2.setVisibility(View.GONE);
        binding.numDadoPlayer1.setVisibility(View.GONE);
        binding.numDadoPlayer2.setVisibility(View.GONE);
        binding.lanzar.setVisibility(View.GONE);
        binding.hold.setVisibility(View.GONE);
        binding.ganador.setVisibility(View.VISIBLE);
        binding.ganador.setText("ENHORABUENA, " + jugador + ", HAS GANADO");
        binding.ganador.setBackgroundResource(R.color.gold);
        binding.nuevaPartida.setVisibility(View.VISIBLE);
    }

    // Reinicia el juego
    public void nuevaPartida(View view){
        // Reinicia las puntuaciones
        totalPlayer1 = 0;
        totalPlayer2 = 0;
        currentScore = 0;
        playerTurn = "Player1";

        // Reinicia las vistas
        binding.imgDado.setImageResource(R.drawable.dado_1);
        binding.totalPlayer1.setText(String.valueOf(totalPlayer1));
        binding.totalPlayer2.setText(String.valueOf(totalPlayer2));
        binding.numDadoPlayer1.setText(String.valueOf(0));
        binding.numDadoPlayer2.setText(String.valueOf(0));

        // Restablece el fondo de los jugadores
        binding.viewUp.setBackgroundResource(R.color.azul_500);
        binding.viewDown.setBackgroundResource(R.color.azul_100);

        // Muestra de nuevo los elementos de la interfaz
        binding.imgDado.setVisibility(View.VISIBLE);
        binding.totalPlayer1.setVisibility(View.VISIBLE);
        binding.totalPlayer2.setVisibility(View.VISIBLE);
        binding.numDadoPlayer1.setVisibility(View.VISIBLE);
        binding.numDadoPlayer2.setVisibility(View.VISIBLE);
        binding.lanzar.setVisibility(View.VISIBLE);
        binding.hold.setVisibility(View.VISIBLE);

        // Oculta el mensaje de ganador y el botón de Nueva partida
        binding.ganador.setVisibility(View.GONE);
        binding.nuevaPartida.setVisibility(View.GONE);
    }
}