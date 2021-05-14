package hse.org.ddmukhin.klavogonkiapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import hse.org.ddmukhin.klavogonkiapplication.databinding.ActivityMainBinding
import moxy.MvpActivity

class MainMenuActivity : MvpActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.beginGame.setOnClickListener {
            if(binding.name.text.isBlank()) {
                Toast.makeText(applicationContext, "Необходимо ввести имя!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("username", binding.name.text)
            startActivity(intent)
            finish()
        }
    }

}